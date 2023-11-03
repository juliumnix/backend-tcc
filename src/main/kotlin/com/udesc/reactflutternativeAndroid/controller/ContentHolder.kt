package com.udesc.reactflutternativeAndroid.controller

import org.springframework.stereotype.Component

@Component
class ContentHolder {
    private val contentMap = mutableMapOf<String, String>()

    @Synchronized
    fun getContent(id: String): String {
        if (contentMap.containsKey(id)) {
            return contentMap.getOrDefault(id, "{\"message\":\"Iniciando para ${id}\"}")
        }
        contentMap[id] = "{\"message\":\"Iniciando para ${id}\"}"
        return contentMap.getOrDefault(id, "{\"message\":\"Iniciando para ${id}\"}")
    }

    @Synchronized
    fun updateContent(id: String, novoConteudo: String) {
        println(contentMap.toString())
        contentMap[id] = novoConteudo
    }

    @Synchronized
    fun removeContent(id: String): Boolean {
        println(contentMap.toString())
        return contentMap.remove(id) != null
    }


}