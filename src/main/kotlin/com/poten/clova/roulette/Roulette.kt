package com.poten.clova.roulette

import java.math.BigDecimal

data class Roulette(
    val point: Int,
    val valueOf: BigDecimal,
    var stock: Int,  // AtomicInteger를 Int로 변경
    var soldOut: Boolean
) {
    val probability: BigDecimal = valueOf
    fun decreaseStock() {
        if (stock > 0) {
            stock -= 1
        }
    }
}

