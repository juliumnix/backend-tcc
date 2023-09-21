package com.udesc.reactflutternativeAndroid.controller

import com.udesc.reactflutternativeAndroid.engine.EngineOrchestrator
import com.udesc.reactflutternativeAndroid.engine.NotifierService
import com.udesc.reactflutternativeAndroid.model.ProjectArtifact
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
import java.io.File
import java.io.FileInputStream
import java.io.Serializable
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@RestController
@EnableAsync
class GenerateProject @Autowired constructor(private val engineOrchestrator: EngineOrchestrator, private val notifierService: NotifierService) {

    @Value("\${git.localCloneDirectory}")
    private val localCloneDirectory: String? = null



    @PostMapping("/create")
    fun createProject(@RequestBody projectRequest: ProjectArtifact): ResponseEntity<out Serializable> {
        val randomName = projectRequest.id
        notifierService.createOrUpdateNotifier(projectRequest.id, "Criando projeto **0%")
        Thread.sleep(2000)
        val projectDirectory = File("$localCloneDirectory/${projectRequest.id}")
        engineOrchestrator.init(
                projectRequest.architecture,
                "$localCloneDirectory/${projectRequest.id}",
                projectRequest.reactDependencies,
                projectRequest.flutterDependencies,
                projectRequest.repositoryKey,
                projectRequest.name,
                projectRequest.ownerName,
                projectRequest.needZIPFile,
                projectRequest.id)
        notifierService.createOrUpdateNotifier(projectRequest.id, "Projeto criado e enviado para o github **90%")
        Thread.sleep(2000)
        if (projectRequest.needZIPFile) {
            val byteArrayOutputStream = ByteArrayOutputStream()

            val zipFileNameToUse = "project.zip"
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

            val byteArray = byteArrayOutputStream.toByteArray()
            val headers = HttpHeaders()
            headers.contentType = MediaType.parseMediaType("application/zip")
            headers.contentLength = byteArray.size.toLong()

            println("Tamanho do arquivo ZIP: ${byteArray.size} bytes")
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$zipFileNameToUse\"")
            notifierService.createOrUpdateNotifier(projectRequest.id, "ZIP Criado **100%")
            Thread.sleep(2000)
            engineOrchestrator.deleteClonedRepository("$localCloneDirectory/$randomName")
            notifierService.deleteNotifier(projectRequest.id)
            println("Projeto zip criado")
            return ResponseEntity(byteArray, headers, 200)
        } else {
            notifierService.createOrUpdateNotifier(projectRequest.id, "O Projeto foi corretamente enviado para o Github **100%")
            Thread.sleep(2000)
            engineOrchestrator.deleteClonedRepository("$localCloneDirectory/$randomName")
            notifierService.deleteNotifier(projectRequest.id)
            val message = "O Projeto foi corretamente enviado para o Github"
            return ResponseEntity.ok(message)
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