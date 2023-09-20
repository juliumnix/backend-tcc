package com.udesc.reactflutternativeAndroid.model

import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.io.IOException

class Notifier(id: String, baseDiretory: String) {

    private val localLogDirectory: String;

    init {
        this.localLogDirectory = baseDiretory
        createLogDirectory(id)

    }

    fun setNotifyStatus(id: String, value: String) {
        val logFileName = "$localLogDirectory/$id.txt"
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
        val logFileName = "$localLogDirectory/$id.txt"
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
        val logFileName = "$localLogDirectory/$id.txt"
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
        val logFileName = "$localLogDirectory/$id.txt"
        print(logFileName)
        try {
            File(logFileName).createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
