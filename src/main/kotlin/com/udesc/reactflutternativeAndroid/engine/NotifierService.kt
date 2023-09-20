package com.udesc.reactflutternativeAndroid.engine

import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.utils.LocalNotifierPath
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.nio.file.Paths

@Service
class NotifierService() {

    private val notifiers = mutableMapOf<String, Notifier>()

    fun createOrUpdateNotifier(id: String, status: String) {
        print("baseDirectory: ${Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "logs").toString()}")
        val existingNotifier = notifiers[id]
        if (existingNotifier != null) {
            existingNotifier.setNotifyStatus(id, status)
        } else {
            val newNotifier = Notifier(id)
            newNotifier.setNotifyStatus(id, status)
            notifiers[id] = newNotifier
        }
    }

    fun getNotifier(id: String): Notifier? {
        return notifiers[id]
    }

    fun deleteNotifier(id: String) {
        println("Deletando log com id $id")
        notifiers[id]?.deleteLog(id)
        notifiers.remove(id)
    }
}