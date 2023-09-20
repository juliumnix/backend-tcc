package com.udesc.reactflutternativeAndroid.engine

import com.udesc.reactflutternativeAndroid.model.Notifier
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.util.Base64
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class DeployProcess() {
    private val client = OkHttpClient()

    fun createRepository(repositoryName: String, description: String, githubToken: String) {
        val url = "https://api.github.com/user/repos"
        val json = """
            {
                "name": "$repositoryName",
                "description": "$description"
            }
        """.trimIndent()

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val request = Request.Builder()
                .url(url)
                .header("Authorization", "token $githubToken")
                .post(requestBody)
                .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                println("Repositório criado com sucesso.")
            } else {
                println("Falha ao criar o repositório. Código de resposta: ${response.code}")
            }
        }
    }

}