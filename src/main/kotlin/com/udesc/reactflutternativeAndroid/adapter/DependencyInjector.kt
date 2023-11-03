package com.udesc.reactflutternativeAndroid.adapter

interface DependencyInjector {
    fun injection(destinationPath: String, directoryName: String, dependencies: List<Map<String, String>>)
}