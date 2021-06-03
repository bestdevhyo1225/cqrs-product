package com.hs.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/products"])
class QueryController {

    @GetMapping(value = ["{id}"])
    fun find(@PathVariable(value = "id") productId: Long) {
        println("OK")
    }
}
