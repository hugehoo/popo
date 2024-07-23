package com.poten.clova.service


import com.poten.clova.dto.ChatCompletionRequest
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader


@FeignClient(name = "clovaService", url = "https://clovastudio.stream.ntruss.com/testapp/v1")
interface ClovaServiceClient {

    @PostMapping("/chat-completions/HCX-003")
    @Headers("Content-Type: application/json", "Accept: text/event-stream")
    fun getChatCompletion(
        @RequestHeader("X-NCP-CLOVASTUDIO-API-KEY") apiKey: String,
        @RequestHeader("X-NCP-APIGW-API-KEY") apiGwKey: String,
        @RequestHeader("X-NCP-CLOVASTUDIO-REQUEST-ID") requestId: String,
        @RequestBody request: ChatCompletionRequest
    ): String
}