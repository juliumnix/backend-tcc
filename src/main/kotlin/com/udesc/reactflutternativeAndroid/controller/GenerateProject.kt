package com.udesc.reactflutternativeAndroid.controller

import com.udesc.reactflutternativeAndroid.engine.EngineOrchestrator
import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.model.ProjectArtifact
import com.udesc.reactflutternativeAndroid.utils.ClientsSinks
import com.udesc.reactflutternativeAndroid.utils.RandomizerName
import okhttp3.internal.http.HttpMethod
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.Serializable
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.random.Random

@RestController
@EnableAsync
class GenerateProject @Autowired constructor(
    private val engineOrchestrator: EngineOrchestrator,
    private val serverEventsController: ServerEventsController
) {
    private val notifier: Notifier = Notifier;

    @Value("\${git.localCloneDirectory}")
    private val localCloneDirectory: String? = null


    @PostMapping("/create")
    fun createProject(@RequestBody projectRequest: ProjectArtifact): ResponseEntity<out Serializable> {
        notifier.setNotifyStatus("Criando projeto")
        ClientsSinks.setContent(projectRequest.id, "{\"message\":\"Iniciando o projeto\"}")
        serverEventsController.atualizarConteudo(projectRequest.id, "{\"message\":\"Iniciando o projeto\"}")
        val randomName = RandomizerName.generateRandomName(10)

        val projectDirectory = File("$localCloneDirectory/$randomName")
        engineOrchestrator.init(
            projectRequest.architecture,
            "$localCloneDirectory/${randomName}",
            projectRequest.reactDependencies,
            projectRequest.flutterDependencies,
            projectRequest.repositoryKey,
            projectRequest.name,
            projectRequest.ownerName,
            projectRequest.needZIPFile
        )

        serverEventsController.atualizarConteudo(projectRequest.id, "{\"message\":\"Projeto criado\"}")

        if (projectRequest.needZIPFile) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val zipFileNameToUse = "${projectRequest.id}-project.zip"
            val zipFilePath = "$localCloneDirectory/$zipFileNameToUse"
            val zipOutputStream = ZipOutputStream(byteArrayOutputStream)


            if (!projectDirectory.exists() || !projectDirectory.isDirectory || projectDirectory.list().isEmpty()) {
                return ResponseEntity.badRequest().body("O diretório de origem está vazio.")
            }

            val targetDirectoryName = findTargetDirectory(projectDirectory)
            if (targetDirectoryName == null) {
                return ResponseEntity.badRequest().body("Nenhum diretório correspondente foi encontrado.")
            }

            try {
                val targetDirectory = File(projectDirectory, targetDirectoryName)
                if (targetDirectory.exists() && targetDirectory.isDirectory) {
                    val filesToZip = targetDirectory.listFiles()
                    if (filesToZip != null) {
                        for (file in filesToZip) {
                            addToZip(zipOutputStream, file, file.name)
                        }
                    }
                } else {
                    return ResponseEntity.badRequest().body("O diretório '$targetDirectoryName' não foi encontrado.")
                }
            } catch (e: Exception) {
                return ResponseEntity.badRequest().body("Falha ao criar o arquivo ZIP.")
            } finally {
                zipOutputStream.close()
            }

            val zipFile = File(zipFilePath)
            FileUtils.writeByteArrayToFile(zipFile, byteArrayOutputStream.toByteArray())


            val byteArray = byteArrayOutputStream.toByteArray()
            val headers = HttpHeaders()
            headers.contentType = MediaType.parseMediaType("application/zip")
            headers.contentLength = byteArray.size.toLong()

            println("Tamanho do arquivo ZIP: ${byteArray.size} bytes")
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$zipFileNameToUse\"")
            engineOrchestrator.deleteClonedRepository("$localCloneDirectory/$randomName")
            println("Projeto zip criado")
            serverEventsController.atualizarConteudo(
                projectRequest.id,
                "{\"message\":\"Projeto criado e enviado para o Github, aproveite =)\"}"
            )
            val message = "O Projeto foi corretamente enviado para o Github"
            return ResponseEntity.ok(message)
        } else {
            engineOrchestrator.deleteClonedRepository("$localCloneDirectory/$randomName")
            val message = "O Projeto foi corretamente enviado para o Github"
            serverEventsController.atualizarConteudo(
                projectRequest.id,
                "{\"message\":\"Projeto criado e enviado para o Github, aproveite =)\"}"
            )
            return ResponseEntity.ok(message)
        }
    }


    @GetMapping("/{id}/download-zip")
    @ResponseBody
    fun downloadZip(@PathVariable id: String): ResponseEntity<ByteArray> {
        val zipFileName = "$id-project.zip"
        val zipFilePath = "$localCloneDirectory/$zipFileName"
        val zipFile = File(zipFilePath)

        if (!zipFile.exists()) {
            return ResponseEntity.notFound().build()
        }

        val fileBytes = FileUtils.readFileToByteArray(zipFile)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_OCTET_STREAM
        headers.contentLength = fileBytes.size.toLong()
        headers.setContentDispositionFormData("attachment", zipFileName)

        // Exclua o arquivo ZIP após o download
        if (zipFile.delete()) {
            return ResponseEntity(fileBytes, headers, HttpStatus.OK)
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    private fun findTargetDirectory(baseDirectory: File): String? {
        val possibleDirectories = baseDirectory.listFiles { file ->
            file.isDirectory && file.name.contains("blank-project-main") || file.isDirectory && file.name.contains("completo")
        }
        return possibleDirectories?.firstOrNull()?.name
    }

    private fun addToZip(zipOutputStream: ZipOutputStream, file: File, entryName: String) {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (subFile in files) {
                    addToZip(zipOutputStream, subFile, entryName + File.separator + subFile.name)
                }
            }
        } else {
            val entry = ZipEntry(entryName)
            zipOutputStream.putNextEntry(entry)
            val fileInputStream = FileInputStream(file)
            fileInputStream.copyTo(zipOutputStream)
            fileInputStream.close()
            zipOutputStream.closeEntry()
        }
    }
}
