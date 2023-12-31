package com.udesc.reactflutternativeAndroid.engine

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.stereotype.Service

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