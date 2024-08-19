package com.poten.clova.roulette

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger


class RouletteAtomic(val score: Int, valueOf: BigDecimal, var stocks: AtomicInteger) {
    val probability: BigDecimal = valueOf
    fun decreaseStock() {
        if (stocks.get() > 0) {
            stocks.set(stocks.get() - 1)
        }
    }
}
