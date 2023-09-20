package com.udesc.reactflutternativeAndroid.model

import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.io.IOException

class Notifier(id: String) {
    private val logDirectory: String = "src/main/resources/logs"

    init {
        createLogDirectory(id)
    }

    fun setNotifyStatus(id: String, value: String) {
        val logFileName = "$logDirectory/$id.txt"
        try {
            val logFile = File(logFileName)
            if (logFile.exists()) {
                File(logFileName).writeText(value)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getNotifyStatus(id: String): String {
        val logFileName = "$logDirectory/$id.txt"
        val logFile = File(logFileName)
        if (logFile.exists()) {
            try {
                return logFile.readText()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    fun deleteLog(id: String) {
        val logFileName = "$logDirectory/$id.txt"
        val logFile = File(logFileName)
        if (logFile.exists()) {
            try {
                logFile.delete()
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    private fun createLogDirectory(id: String) {
        val logFileName = "$logDirectory/$id.txt"
        try {
            File(logFileName).createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
