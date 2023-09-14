package com.udesc.reactflutternativeAndroid.engine

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.udesc.reactflutternativeAndroid.adapter.DependencyInjector
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
                dependenciesNode.put(name, version)
            }

            FileWriter(packageJsonFile).use { writer ->
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, packageJsonNode)
            }
        } catch (exception: Exception) {
            throw RuntimeException(exception.message)
        }
    }
}