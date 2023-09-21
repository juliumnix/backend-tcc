package com.udesc.reactflutternativeAndroid.engine

import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.utils.GithubActionGenerator
import com.udesc.reactflutternativeAndroid.utils.ReadmeGenerator
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.io.File
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.Base64
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Service
class EngineOrchestrator @Autowired constructor(
    private val reactDependencyInjection: ReactDependencyInjection,
    private val flutterDependencyInjection: FlutterDependencyInjection,
    private val deployProcess: DeployProcess,
    private val changeNameProject: ChangeNameProject) {

    val sendToGithub = SendToGithub();
    val notifier = Notifier

    fun init(
        arquitecture: String,
        destinationPath: String,
        reactDependencies: List<Map<String, String>>,
        flutterDependencies: List<Map<String, String>>,
        repositoryKey: String,
        projectName: String,
        ownerName: String,
        needZip: Boolean,
    ) {
        val repositoryUrl: String = when (arquitecture) {
            "mvvm" -> "https://github.com/juliumnix/mvvm-blank-project/archive/refs/heads/main.zip"
            "mvp" -> "https://github.com/juliumnix/mvp-blank-project/archive/refs/heads/main.zip"
            "completo" -> "https://github.com/juliumnix/projeto-completo/archive/refs/heads/main.zip"
            else -> ""
        }
        val url = URL(repositoryUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        try {
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val zipStream = connection.inputStream
                val zipInputStream = ZipInputStream(zipStream)

                val buffer = ByteArray(1024)
                var entry: ZipEntry? = zipInputStream.nextEntry
                val diretoryName = entry?.name;
                while (entry != null) {
                    val entryFile = File(destinationPath, entry.name)
                    notifier.setNotifyStatus("Copiando arquivos localmente para modificação")
                    if (entry.isDirectory) {
                        entryFile.mkdirs()
                    } else {
                        val output = FileOutputStream(entryFile)
                        var len: Int
                        while (zipInputStream.read(buffer).also { len = it } > 0) {
                            output.write(buffer, 0, len)
                        }
                        output.close()
                    }

                    zipInputStream.closeEntry()
                    entry = zipInputStream.nextEntry
                }

                zipInputStream.close()


                if (diretoryName != null) {
                    if (arquitecture != "completo") {
                        notifier.setNotifyStatus("Injetando dependencias do React")
                        ReadmeGenerator.setReactTable("\n" +
                                "# Dependencias\n" +
                                "\n" +
                                "React Native\n" +
                                "\n" +
                                "| Dependencia |  Versão  |\n" +
                                "|:-----|:--------:|\n")
                        reactDependencyInjection.injection(destinationPath, diretoryName, reactDependencies)
                        ReadmeGenerator.setFlutterTable("Flutter\n" +
                                "\n" +
                                "| Dependencia |  Versão  |\n" +
                                "|:-----|:--------:|\n")
                        flutterDependencyInjection.injection(destinationPath, diretoryName, flutterDependencies)
                        changeNameProject.changeSettingsGradle("$destinationPath/$diretoryName", projectName)
                        changeNameProject.changeStringXML("$destinationPath/$diretoryName", projectName)
                    }

                    val readmeFile = File("$destinationPath/$diretoryName", "README.md")
                    val readmeContent = ReadmeGenerator.generate();
                    FileWriter(readmeFile).use { writer ->
                        writer.write(readmeContent)
                    }

                    ReadmeGenerator.setReactTable("")
                    ReadmeGenerator.setFlutterTable("")

                    deployProcess.createRepository(projectName, "", repositoryKey)
                }
                val workflowsDir = File("$destinationPath/$diretoryName/.github/workflows")
                if (!workflowsDir.exists()) {
                    workflowsDir.mkdirs()
                }


                val readmeFileGithubActions = File("$destinationPath/$diretoryName", "/.github/workflows/build.yml")
                val readmeContentGithubActions = GithubActionGenerator.generate();
                FileWriter(readmeFileGithubActions).use { writer ->
                    writer.write(readmeContentGithubActions)
                }

                sendToGithub.commitProjectGithub(
                    "$destinationPath/$diretoryName",
                    repositoryKey,
                    ownerName,
                    projectName);


            } else {
                throw RuntimeException("Failed to download the GitHub zip file. Response code: ${connection.responseCode}")
            }
        } finally {
            connection.disconnect()
        }
    }


    @Throws(IOException::class)
    fun deleteClonedRepository(pathRepository: String) {
        val path = File(pathRepository)
        deleteDirectory(path.toPath())
    }

    private fun deleteDirectory(directory: Path?) {
        Files.walk(directory).sorted(Comparator.reverseOrder()).map { it.toFile() }.forEach { it.delete() }
    }


}