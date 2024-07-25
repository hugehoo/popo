package com.poten.clova.dto

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

data class MyPage(val messages: Page<MessageDto>)

