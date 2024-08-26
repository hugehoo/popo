package com.poten.clova.roulette

import org.redisson.api.RBucket
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisTemplate(
    private val redissonClient: RedissonClient
) {
    private val rouletteKey: String = "roulette"

    fun getBucket(): RBucket<List<Roulette>>? {
        return redissonClient.getBucket<List<Roulette>>(rouletteKey)
    }

    fun <T> executeWithLock(lockName: String, action: () -> T): T {
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
}
