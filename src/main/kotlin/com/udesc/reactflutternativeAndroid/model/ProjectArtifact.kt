package com.udesc.reactflutternativeAndroid.model

data class ProjectArtifact(
        val name: String,
        val ownerName: String,
        val repositoryKey: String,
        val organization: String,
        val architecture: String,
        val reactDependencies: List<Map<String, String>>,
        val flutterDependencies: List<Map<String, String>>,
        val needZIPFile: Boolean
)