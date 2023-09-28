package com.udesc.reactflutternativeAndroid.controller

import com.udesc.reactflutternativeAndroid.model.responses.StatusResponse
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class AvailableAPI {

    @GetMapping("/available")
    fun getAvailableAPI(): StatusResponse {
        return StatusResponse(true);
    }
}