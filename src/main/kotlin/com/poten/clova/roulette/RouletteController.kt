package com.poten.clova.roulette

import com.poten.clova.popo.dto.ObjectDto
import com.poten.clova.popo.dto.ResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class RouletteController(private val rouletteService: RouletteService) {

    @GetMapping("/roulette")
    fun playRoulette(): ResponseEntity<ObjectDto<Roulette>> {
        val roulette: Roulette = rouletteService.play()
        return ResponseEntity.ok()
            .body(ObjectDto(roulette))
    }

    @GetMapping("/init-roulette")
    fun initRoulette(): ResponseEntity<ResponseDto<Roulette>> {
        val user = rouletteService.initRoulette()
        return ResponseEntity.ok()
            .body(ResponseDto())
    }


}