package com.poten.clova.roulette

import org.redisson.api.RBucket
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.TimeUnit


@Service
class RouletteService(
    private val redissonClient: RedissonClient
) {
    private val rouletteKey: String = "roulette"
    private val random = Random()


    fun initRoulette(): RBucket<List<Roulette>>? {
        val dailyRoulette: List<Roulette> = listOf(
            Roulette(10, BigDecimal.valueOf(0.5), 40, false),
            Roulette(50, BigDecimal.valueOf(0.3), 30, false),
            Roulette(100, BigDecimal.valueOf(0.1), 16, false),
            Roulette(500, BigDecimal.valueOf(0.05), 8, false),
            Roulette(800, BigDecimal.valueOf(0.03), 4, false),
            Roulette(1000, BigDecimal.valueOf(0.02), 2, false)
        )
        val bucket = getRouletteItems()
        bucket.set(dailyRoulette)
        return bucket
    }
    fun play(): Roulette {
        return executeWithLock("rouletteLock", ::playRoulette)
    }

    private fun <T> executeWithLock(lockName: String, action: () -> T): T {
        //TODO : Lockname 이 왜 필요한거지?
        val lock: RLock = redissonClient.getLock(lockName)

        try {
            // 락 획득 시도 (최대 20초 동안 대기, 30초 후 자동 해제)
            val isLocked = lock.tryLock(20, 30, TimeUnit.SECONDS)

            if (isLocked) {
                try {
                    return action()
                } finally {
                    lock.unlock()
                }
            } else {
                throw IllegalStateException("Failed to acquire lock for $lockName")
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw IllegalStateException("Lock acquisition was interrupted", e)
        }
    }

    private fun playRoulette(): Roulette {
        val bucket = getRouletteItems()
        val rouletteList: List<Roulette> = bucket.get()

        val soldOuts = rouletteList.filter { it.soldOut }
        checkPlayAvailable(soldOuts)

        val rouletteProbability = getRouletteProbability(rouletteList, soldOuts)
        val roulette = getResult(rouletteProbability)
        decreaseStock(roulette)
        bucket.set(rouletteList)
        return roulette
    }

    // without lock
//    fun play(): Roulette {
//
//        val bucket = getRouletteItems()
//        val rouletteList: List<Roulette> = bucket.get()
//
//        val soldOuts = rouletteList.filter { it -> it.soldOut }
//        checkPlayAvailable(soldOuts)
//
//        val rouletteProbability = getRouletteProbability(rouletteList, soldOuts)
//        val roulette = getResult(rouletteProbability)
//        decreaseStock(roulette)
//        bucket.set(rouletteList)
//        return roulette
//    }

    private fun getRouletteItems(): RBucket<List<Roulette>> {
        val bucket = redissonClient.getBucket<List<Roulette>>(rouletteKey)
        if (!bucket.isExists) {
            initRoulette()
        }
        return bucket
    }

    private fun getRouletteProbability(
        rouletteAtomicLists: List<Roulette>,
        soldOuts: List<Roulette>
    ): TreeMap<BigDecimal, Roulette> {
        val remainProbability = getCurrentProbability(rouletteAtomicLists, soldOuts)
        var cumulativeProbability = BigDecimal.ZERO
        val probabilityMap = TreeMap<BigDecimal, Roulette>()

        rouletteAtomicLists
            .filter { r -> r.stock != 0 }
            .forEach { r ->
                val add = r.probability.add(remainProbability)
                cumulativeProbability = cumulativeProbability.add(add)
                probabilityMap[cumulativeProbability] = r
            }
        return probabilityMap
    }

    private fun getResult(rouletteAtomicMap: TreeMap<BigDecimal, Roulette>): Roulette {
        val randomValue = BigDecimal.valueOf(random.nextDouble())
        return rouletteAtomicMap.higherEntry(randomValue).value
    }

    private fun decreaseStock(roulette: Roulette) {
        roulette.decreaseStock()
        if (roulette.stock == 0) roulette.soldOut = true
    }

    private fun getCurrentProbability(
        rouletteAtomicLists: List<Roulette>,
        soldOuts: List<Roulette>,
    ): BigDecimal? {
        val sumSoldOutProbability = soldOuts
            .stream()
            .map { obj: Roulette -> obj.probability }
            .reduce(BigDecimal.ZERO)
            { obj: BigDecimal, agent: BigDecimal? -> obj.add(agent) }
        val availableSize = rouletteAtomicLists.size - soldOuts.size
        return sumSoldOutProbability.divide(BigDecimal.valueOf(availableSize.toLong()), 3, RoundingMode.HALF_UP)
    }

    private fun checkPlayAvailable(soldOuts: List<Roulette>) {
        if (soldOuts.size == 6) {
            throw StockException("Out of Stock Exception")
        }
    }
}