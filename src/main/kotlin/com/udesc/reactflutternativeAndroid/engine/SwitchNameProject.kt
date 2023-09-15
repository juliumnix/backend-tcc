package com.udesc.reactflutternativeAndroid.engine

import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter
import java.io.IOException

@Service
class SwitchNameProject {
    fun changeSettingsGradle(basePath: String, newName: String) {
        val pastaDoProjeto = findAndroidPackage(basePath)

        if (pastaDoProjeto != null) {
            try {
                val caminhoSettingsGradle = File(pastaDoProjeto, "settings.gradle")

                if (caminhoSettingsGradle.exists() && caminhoSettingsGradle.isFile) {
                    val lines = caminhoSettingsGradle.readLines().toMutableList()

                    for (i in lines.indices) {
                        if (lines[i].startsWith("rootProject.name")) {
                            lines[i] = "rootProject.name = '$newName'"
                            break
                        }
                    }
                    val writer = FileWriter(caminhoSettingsGradle)
                    for (line in lines) {
                        writer.write("$line\n")
                    }
                    writer.close()

                    println("Nome no settings.gradle alterado para '$newName'")
                } else {
                    println("O arquivo settings.gradle não foi encontrado em $caminhoSettingsGradle")
                }
            } catch (e: IOException) {
                println("Erro ao alterar o nome no settings.gradle: ${e.message}")
            }
        } else {
            println("Pasta do projeto Android não encontrada.")
        }
    }

    private fun findAndroidPackage(basePath: String): File? {
        val pastaBase = File(basePath)

        if (pastaBase.exists() && pastaBase.isDirectory) {
            val subpastas = pastaBase.listFiles()

            if (subpastas != null) {
                for (subpasta in subpastas) {
                    if (subpasta.isDirectory) {
                        val settingsGradleFile = File(subpasta, "settings.gradle")
                        if (settingsGradleFile.exists() && settingsGradleFile.isFile) {
                            return subpasta
                        }
                    }
                }
            }
        }

        return null
    }


}