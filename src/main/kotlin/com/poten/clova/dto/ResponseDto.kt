package com.poten.clova.dto


open class ResponseDto<T>(
    var code: Int = 200,
    var message: String = "success!",
    var data: T? = null
)