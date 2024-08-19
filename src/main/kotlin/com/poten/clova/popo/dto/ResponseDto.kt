package com.poten.clova.popo.dto


open class ResponseDto<T>(
    var code: Int = 200,
    var message: String = "success!",
    var data: T? = null
)