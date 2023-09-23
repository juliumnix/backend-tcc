package com.udesc.reactflutternativeAndroid.controller

import com.udesc.reactflutternativeAndroid.model.responses.RemoveSSE
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.Duration


@RestController
@RequestMapping("/server-events")
@CrossOrigin("*")
class ServerEventsController(private val contentHolder: ContentHolder) {

    @GetMapping("/{id}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getEvents(@PathVariable id: String): Flux<ServerSentEvent<String?>> {
        val initialContent = contentHolder.getContent(id)
        return disparaEvento(id, initialContent)
    }


    fun disparaEvento(id: String, initialContent: String): Flux<ServerSentEvent<String?>> {
        return Flux.interval(Duration.ofMillis(300))
            .map {
                val conteudo = contentHolder.getContent(id)
                ServerSentEvent.builder(conteudo)
                    .event("lineEvent")
                    .retry(Duration.ofMillis(1000))
                    .build()
            }
    }

    fun atualizarConteudo(id: String, novoConteudo: String) {
        contentHolder.updateContent(id, novoConteudo)
    }


    @GetMapping("/remove/{id}")
    fun removerConteudo(@PathVariable id: String): ResponseEntity<RemoveSSE> {
        if (contentHolder.removeContent(id)) {
            val removeSSE = RemoveSSE(true)
            return ResponseEntity.status(HttpStatus.OK).body(removeSSE)
        }
        val removeSSE = RemoveSSE(false)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(removeSSE)
    }
}




