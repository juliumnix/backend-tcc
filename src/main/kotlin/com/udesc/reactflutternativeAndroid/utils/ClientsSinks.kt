package com.udesc.reactflutternativeAndroid.utils

import java.util.concurrent.ConcurrentHashMap

object ClientsSinks {

    private val clientSinks: MutableMap<String, String> = ConcurrentHashMap()

    fun getContent(id: String): String? {
        return clientSinks[id]
    }

    fun setContent(id: String, content: String) {
        clientSinks[id] = content
    }

    fun addClientSinks(id: String, content: String) {
        val valor = clientSinks[id]
        if (valor == null) {
            clientSinks[id] = content
        }
    }

    fun removeClientsSinks(id: String, content: String) {
        clientSinks.remove(id, content)
    }
}