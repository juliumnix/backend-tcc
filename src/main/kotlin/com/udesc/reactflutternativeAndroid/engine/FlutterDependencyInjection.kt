package com.udesc.reactflutternativeAndroid.engine

import com.udesc.reactflutternativeAndroid.adapter.DependencyInjector
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import java.io.FileWriter

@Service
class FlutterDependencyInjection : DependencyInjector {

    override fun injection(destinationPath: String, diretoryName: String, dependencies: List<Map<String, String>>) {
        try {
            val flutterModuleDirectory = File("$destinationPath/$diretoryName", "flutter_module")
            val pubspecYamlFile = File(flutterModuleDirectory, "pubspec.yaml")

            if (!pubspecYamlFile.exists()) {
                throw RuntimeException("pubspec.yaml file not found in the flutter_module directory.")
            }

            val yaml = Yaml()
            val existingData: MutableMap<String, Any?> = try {
                val reader = FileReader(pubspecYamlFile)
                yaml.load(reader)
            } catch (e: Exception) {
                mutableMapOf()
            }

            val dependenciesNodeFlutter = existingData.getOrPut("dependencies") { mutableMapOf<String, Any?>() } as MutableMap<String, Any?>

            dependencies.forEach { dependency ->
                val name = dependency["name"] as String
                val version = dependency["version"] as String
                dependenciesNodeFlutter[name] = version
            }

            val writer = FileWriter(pubspecYamlFile)
            val options = DumperOptions()
            options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            val dumper = Yaml(options)
            dumper.dump(existingData, writer)
            writer.close()
        } catch (exception: Exception) {
            throw RuntimeException(exception.message)
        }
    }
}