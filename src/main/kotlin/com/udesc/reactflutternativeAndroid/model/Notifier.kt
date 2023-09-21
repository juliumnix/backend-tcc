package com.udesc.reactflutternativeAndroid.model

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class Notifier(id: String) {

    init {
        createLogDirectory(id)

    }

    fun setNotifyStatus(id: String, value: String) {
        val logFileName = "src/main/resources/$id.txt"
        try {
            val logFile = File(logFileName)
            if (logFile.exists()) {
                logFile.writeText("")
                BufferedWriter(FileWriter(logFile, true)).use { writer ->
                    writer.append(value)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getNotifyStatus(id: String): String {
        val logFileName = "src/main/resources/$id.txt"
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
        val logFileName = "src/main/resources/$id.txt"
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
        val logFileName = "src/main/resources/$id.txt"
        print(logFileName)
        try {
            File(logFileName).createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
