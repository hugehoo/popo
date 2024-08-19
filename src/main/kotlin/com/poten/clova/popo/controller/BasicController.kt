package com.poten.clova.popo.controller

import com.poten.clova.dto.*
import com.poten.clova.popo.dto.*
import com.poten.clova.roulette.RouletteService
import com.poten.clova.popo.service.ClovaService
import com.poten.clova.popo.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class BasicController(private val userService: UserService,
                      private val rouletteService: RouletteService,
                      private val clovaService: ClovaService
) {

    @GetMapping("/health-check")
    fun basicController(): String {
        return "healthy"
    }

    @GetMapping("/my-page")
    fun getMyPage(@RequestParam(required = true) deviceId: String, pageable: Pageable): ResponseEntity<ResponseDto<Page<MessageDto>>> {
        val user = userService.getMyPage(deviceId, pageable)
        return ResponseEntity.ok()
            .body(ObjectDto(user))
    }

    @PatchMapping("/my-page/name")
    fun updateUserName(@RequestBody onboard: OnboardDto): ResponseEntity<ResponseDto<String>> {
        val user = userService.updateName(onboard)
        return ResponseEntity.ok()
            .body(ObjectDto())
    }

    @GetMapping("/onboard")
    fun onBoardController(@RequestParam(required = true) deviceId: String ): ResponseEntity<ResponseDto<Onboard>> {
        val user = userService.getUserById(deviceId)
        return ResponseEntity.ok()
            .body(ObjectDto(user))
    }

    @PostMapping("/onboard")
    fun onBoardController(@RequestBody onboard: OnboardDto): ResponseEntity<ResponseDto<String>> {
        userService.saveUser(onboard)
        return ResponseEntity.ok()
            .body(ResponseDto())
    }

    @PostMapping("/message")
    fun messageController(@RequestBody userMessage: UserMessage): ResponseEntity<ResponseDto<ClovaMessage>> {
        val clovaResponse = clovaService.sendMessage(userMessage)
        return ResponseEntity.ok()
            .body(ObjectDto(clovaResponse))
    }

    @PostMapping("/charm")
    fun charmController(@RequestBody userMessage: UserMessage): ResponseEntity<ResponseDto<CharmDto>> {
        val clovaResponse = clovaService.generateCharm(userMessage)
        return ResponseEntity.ok()
            .body(ObjectDto(clovaResponse))
    }
}