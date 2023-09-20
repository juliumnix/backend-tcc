package com.udesc.reactflutternativeAndroid

import com.udesc.reactflutternativeAndroid.utils.LocalNotifierPath
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactFlutterNativeAndroidApplication

fun main(args: Array<String>) {
    runApplication<ReactFlutterNativeAndroidApplication>(*args)
}
