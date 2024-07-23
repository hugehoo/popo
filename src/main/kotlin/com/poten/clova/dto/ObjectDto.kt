package com.poten.clova.dto


class ObjectDto<T>(data: T? = null) : ResponseDto<T>(
    data = data
)