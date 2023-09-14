package com.udesc.reactflutternativeAndroid.controller

import com.udesc.reactflutternativeAndroid.model.Notifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NotifyController {
    var nofifier = Notifier;

    @GetMapping("/notifier")
    fun getNotifierStatus(): ResponseEntity<String>{
        return ResponseEntity.status(HttpStatus.CREATED).body(nofifier.getNotifyStatus())
    }
}