package com.udesc.reactflutternativeAndroid.model

import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.io.IOException
import java.nio.file.Paths

class Notifier(id: String) {

    init {
        createLogDirectory(id)

    }

    fun setNotifyStatus(id: String, value: String) {
        val logFileName = "${Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "logs")}/$id.txt"
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
        val logFileName = "${Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "logs")}/$id.txt"
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
        val logFileName = "${Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "logs")}/$id.txt"
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
        val logFileName = "${Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "logs").toString()}/$id.txt"
        print(logFileName)
        try {
            File(logFileName).createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
