package com.udesc.reactflutternativeAndroid.adapter

interface DependencyInjector {
    fun injection(destinationPath: String, diretoryName: String, dependencies: List<Map<String, String>>)
}