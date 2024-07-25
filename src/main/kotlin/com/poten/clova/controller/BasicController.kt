package com.poten.clova.controller

import com.poten.clova.dto.*
import com.poten.clova.entity.Message
import com.poten.clova.service.ClovaService
import com.poten.clova.service.UserService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestParam


@RestController
class BasicController(private val userService: UserService,
                      private val clovaService: ClovaService) {

    @GetMapping("/health-check")
    fun basicController(): String {
        return "healthy"
    }

    @GetMapping("/my-page")
    fun getMyPage(pageable: Pageable): ResponseEntity<ResponseDto<Page<MessageDto>>> {
        val user = userService.getMyPage("dserver1x3", pageable)
        return ResponseEntity.ok()
            .body(ObjectDto(user))
    }

    @GetMapping("/onboard")
    fun onBoardController(@RequestParam(required = true) deviceId: String ): ResponseEntity<ResponseDto<Onboard>> {
        val user = userService.getUserById(deviceId)
        return ResponseEntity.ok()
            .body(ObjectDto(user))
    }

    @PostMapping("/onboard")
    fun onBoardController(@RequestBody onboard: Onboard): ResponseEntity<ResponseDto<String>> {
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