package com.udesc.reactflutternativeAndroid.engine

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.udesc.reactflutternativeAndroid.adapter.DependencyInjector
import com.udesc.reactflutternativeAndroid.model.Notifier
import com.udesc.reactflutternativeAndroid.utils.ReadmeGenerator
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter
import java.lang.RuntimeException

@Service
class ReactDependencyInjection : DependencyInjector {

    override fun injection(destinationPath: String, diretoryName: String, dependencies: List<Map<String, String>>) {
        try {
            val packageJsonFile = File("$destinationPath/$diretoryName", "package.json")
            val objectMapper = ObjectMapper()
            val packageJsonNode: ObjectNode = objectMapper.readTree(packageJsonFile) as ObjectNode

            val dependenciesNode: ObjectNode = packageJsonNode.with("dependencies") as ObjectNode

            dependencies.forEach { dependency ->
                val name = dependency["name"]
                val version = dependency["version"]
                ReadmeGenerator.setReactTable(ReadmeGenerator.generateReactTable() + "|  $name  | $version |  \n")
                dependenciesNode.put(name, version)
            }

            ReadmeGenerator.setReactTable(ReadmeGenerator.generateReactTable() + "\n")

            FileWriter(packageJsonFile).use { writer ->
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, packageJsonNode)
            }
        } catch (exception: Exception) {
            throw RuntimeException(exception.message)
        }
    }
}