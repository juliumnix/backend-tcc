package com.udesc.reactflutternativeAndroid.controller

import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.model.responses.NotifierResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NotifyController {
    var notifier = Notifier
    @GetMapping("/notifier")
    fun getStatus(): ResponseEntity<NotifierResponse> {
        val content = notifier.getNotifyStatus()
        val response = NotifierResponse(content)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}