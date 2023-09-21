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
        println(value)
        val logFileName = "src/main/resources/$id.txt"
        try {
            val logFile = File(logFileName)
            if (logFile.exists()) {
                println("entrou aqui no metodo do set")
                logFile.writeText("aaaaaaaaaaaa")
                BufferedWriter(FileWriter(logFile, true)).use { writer ->
                    writer.append(value)
                }
                println("teoricamente ele escreveu")
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
                println(logFile.readText())
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
        try {
            File(logFileName).createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
