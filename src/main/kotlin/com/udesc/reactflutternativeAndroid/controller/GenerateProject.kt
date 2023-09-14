package com.udesc.reactflutternativeAndroid.controller

import com.udesc.reactflutternativeAndroid.engine.EngineOrchestrator
import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.model.ProjectArtifact
import com.udesc.reactflutternativeAndroid.utils.RandomizerName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.random.Random

@RestController
@EnableAsync
class GenerateProject @Autowired constructor(private val engineOrchestrator: EngineOrchestrator) {
    private val notifier: Notifier = Notifier;

    @Value("\${git.localCloneDirectory}")
    private val localCloneDirectory: String? = null


    @PostMapping("/create")
    fun createProject(@RequestBody projectRequest: ProjectArtifact): ResponseEntity<ByteArray> {
        notifier.setNotifyStatus("Criando projeto")
        engineOrchestrator.init(
                projectRequest.architecture,
                "$localCloneDirectory/${RandomizerName.generateRandomName(10)}",
                projectRequest.reactDependencies,
                projectRequest.flutterDependencies,
                projectRequest.repositoryKey,
                projectRequest.name,
                projectRequest.ownerName,
                projectRequest.needZIPFile)

        val content = "Este é um exemplo de string que pode ser retornada."

        // Verifique se o cliente deseja um arquivo ZIP (por exemplo, com base em parâmetros de consulta)
        val acceptHeader = HttpHeaders().get(HttpHeaders.ACCEPT)?.firstOrNull()
        if ("application/zip" == acceptHeader) {
            // Se o cliente aceitar um arquivo ZIP, crie e retorne um arquivo ZIP
            val byteArrayOutputStream = ByteArrayOutputStream()
            val zipOutputStream = ZipOutputStream(byteArrayOutputStream)
            val entry = ZipEntry("data.txt")

            zipOutputStream.putNextEntry(entry)
            zipOutputStream.write(content.toByteArray())
            zipOutputStream.closeEntry()
            zipOutputStream.close()

            val byteArray = byteArrayOutputStream.toByteArray()
            val headers = HttpHeaders()
            headers.contentType = MediaType.parseMediaType("application/zip")
            headers.contentLength = byteArray.size.toLong()

            return ResponseEntity(byteArray, headers, 200)
        } else {
            // Caso contrário, retorne a string simples
            return ResponseEntity.ok(content.toByteArray())
        }

    }
}