package com.poten.clova.roulette

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Testcontainers
class RouletteServiceTest {
    companion object {
        private lateinit var redissonClient: RedissonClient

        @Container
        private val redis = GenericContainer(DockerImageName.parse("redis:6.2.6-alpine"))
            .withExposedPorts(6379)

        @BeforeAll
        @JvmStatic
        fun setup() {
            redis.start()
            val config = Config()
            config.useSingleServer()
                .setAddress("redis://localhost:6379")
            redissonClient = Redisson.create(config)
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            redissonClient.shutdown()
            redis.stop()
        }
    }

    private lateinit var rouletteService: RouletteService

    @BeforeEach
    fun setUp() {
        rouletteService = RouletteService(redissonClient)
        rouletteService.initRoulette()
    }

    @Test
    fun `룰렛 play() 1회당 재고가 1씩 차감된다`() {
        val result = rouletteService.play()

        assertNotNull(result)
        val updatedList = redissonClient.getBucket<List<Roulette>>("roulette").get()
        val updatedRoulette = updatedList.find { it.point == result.point }
        assertNotNull(updatedRoulette)
        assertEquals(updatedRoulette?.stock, result.stock)
    }

    @Test
    fun `모든 재고가 소진되면 예외를 던진다`() {
        val soldOutList = List(6) { Roulette(it * 10, BigDecimal.valueOf(0.1), 0, true) }
        redissonClient.getBucket<List<Roulette>>("roulette").set(soldOutList)

        assertThrows<StockException> { rouletteService.play() }
    }

    @Test
    fun `룰렛 play 동시성 테스트`() {
        val totalPlays = 100
        val executor = Executors.newFixedThreadPool(50)
        val results = ConcurrentHashMap<Int, Int>()
        val latch = CountDownLatch(totalPlays)
        var totalCount: Int = 0;
        repeat(totalPlays) { // Create 100 concurrent play requests
            executor.submit {
                try {
                    val roulette = rouletteService.play()
                    totalCount += 1
                    results.merge(roulette.point, 1) { old, new -> old + new }
                } catch (e: IllegalStateException) {
                    results.merge(-1, 1) { old, new -> old + new }
                } finally {
                    latch.countDown()
                }
            }
        }

        // Wait for all tasks to complete
        latch.await(30, TimeUnit.SECONDS)

        // Shut down the executor
        executor.shutdown()
        executor.awaitTermination(5, TimeUnit.SECONDS)

        // Verify results
        val totalPlayedCount = results.values.sum()
        assertEquals(totalPlays, totalPlayedCount, "Total played count should be $totalPlays")

        val finalRouletteList = redissonClient.getBucket<List<Roulette>>("roulette").get()
        val remainingStock = finalRouletteList.sumOf { it.stock }
        assertEquals(0, remainingStock, "All stock should be depleted")

        // Verify that no more plays are possible
        assertThrows<StockException> { rouletteService.play() }
    }
}