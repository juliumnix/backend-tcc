package com.udesc.reactflutternativeAndroid.engine

import com.udesc.reactflutternativeAndroid.model.Notifier
import org.springframework.stereotype.Service

@Service
class NotifierService {
    private val notifiers = mutableMapOf<String, Notifier>()

    fun createOrUpdateNotifier(id: String, status: String) {
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
        return notifiers.get(id)
    }

    fun deleteNotifier(id: String) {
        println("Deletando log com id ${id}")
        notifiers[id]?.deleteLog(id);
        notifiers.remove(id)
    }
}