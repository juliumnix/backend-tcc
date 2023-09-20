package com.udesc.reactflutternativeAndroid.utils

object LocalNotifierPath {
    private var path: String = ""

    fun setPath(value: String) {
        path = value;
    }

    fun getPath(): String {
        return path
    }

}