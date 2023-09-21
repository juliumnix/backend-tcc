package com.udesc.reactflutternativeAndroid.model

object Notifier {
    private var notifyStatus: String = "";
    fun setNotifyStatus(value: String) {
        notifyStatus = value;
    }

    fun getNotifyStatus(): String {
        return notifyStatus;
    }
}