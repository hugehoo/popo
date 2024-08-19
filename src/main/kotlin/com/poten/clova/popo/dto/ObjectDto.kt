package com.poten.clova.popo.dto


class ObjectDto<T>(data: T? = null) : ResponseDto<T>(
    data = data
)