package com.udesc.reactflutternativeAndroid.engine

import com.udesc.reactflutternativeAndroid.model.Notifier
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service
import java.io.File

@Service
class SendToGithub {
    val client = OkHttpClient()
    fun commitProjectGithub(caminho: String, token: String, ownerName: String, repoName: String) {
        fun processDirectory(directory: File) {
            for (file in directory.listFiles() ?: arrayOf()) {
                if (file.isDirectory) {
                    processDirectory(file)
                } else {
                    processRequest(file, caminho, ownerName, repoName, token)
                }
            }
        }

        val projectDirectory = File(caminho)

        if (projectDirectory.exists() && projectDirectory.isDirectory) {
            processDirectory(projectDirectory)
        } else {
            println("O diretório especificado não existe ou não é um diretório válido.")
        }
    }

    fun processRequest(file: File, caminho: String, ownerName: String, repoName: String, token: String) {
        val content = file.readText()
        val fileName = file.relativeTo(File(caminho)).path.replace('\\', '/')

        val request = Request.Builder().url("https://api.github.com/repos/$ownerName/$repoName/contents/$fileName").header("Authorization", "token $token").build()

        val response = client.newCall(request).execute()
        val responseBody = response.body

        if (response.isSuccessful && responseBody != null) {
            val existingContent = responseBody.string()
            val sha = existingContent.extractSha()

            val requestUpdate = Request.Builder().url("https://api.github.com/repos/$ownerName/$repoName/contents/$fileName").header("Authorization", "token $token").put(createRequestBody(content, sha)).build()

            val responseUpdate = client.newCall(requestUpdate).execute()

            if (responseUpdate.isSuccessful) {
                println("Arquivo $fileName atualizado com sucesso!")
            } else {
                println("Falha ao atualizar o arquivo $fileName")
            }
        } else {
            println("Arquivo $fileName não encontrado no repositório. Criando novo arquivo...")

            val requestCreate = Request.Builder().url("https://api.github.com/repos/$ownerName/$repoName/contents/$fileName").header("Authorization", "token $token").put(createRequestBody(content, null)).build()

            val responseCreate = client.newCall(requestCreate).execute()

            if (responseCreate.isSuccessful) {
                println("Arquivo $fileName criado com sucesso!")
            } else {
                println("Falha ao criar o arquivo $fileName")
            }
        }
    }

    private fun String.extractSha(): String? {
        val shaRegex = "\"sha\":\"([a-f0-9]+)\"".toRegex()
        val matchResult = shaRegex.find(this)
        return matchResult?.groupValues?.get(1)
    }

    private fun createRequestBody(content: String, sha: String?): okhttp3.RequestBody {
        val json = """{
        "message": "Atualizando arquivo",
        "content": "${content.encodeBase64()}",
        "sha": "$sha"
    }"""

        val mediaType = "application/json; charset=utf-8".toMediaType()
        return json.toRequestBody(mediaType)
    }

    private fun String.encodeBase64(): String {
        return java.util.Base64.getEncoder().encodeToString(this.toByteArray())
    }
}
