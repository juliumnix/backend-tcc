package com.udesc.reactflutternativeAndroid.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.udesc.reactflutternativeAndroid.engine.NotifierService
import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.model.responses.NotifierResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.IOException

@RestController
class NotifyController(private val notifierService: NotifierService) {

    @GetMapping("/get/{id}/status")
    fun getNotifyStatus(@PathVariable id: String, response: HttpServletResponse) {
        val notifier = notifierService.getNotifier(id)

        if (notifier != null) {
            val status = notifier.getNotifyStatus(id)

            val jsonResponse = mapOf("status" to status)

            val objectMapper = ObjectMapper()
            val jsonText = objectMapper.writeValueAsString(jsonResponse)

            response.contentType = "application/json"

            try {
                val writer = response.writer
                writer.write(jsonText)
                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            response.status = HttpServletResponse.SC_NOT_FOUND
        }
    }
}