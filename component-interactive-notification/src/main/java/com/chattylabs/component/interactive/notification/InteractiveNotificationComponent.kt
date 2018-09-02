package com.chattylabs.component.interactive.notification

const val DEFAULT_ID = 1

interface InteractiveNotificationComponent {
    fun addNode(node: InteractiveNotification.Node)

    fun getNode(id: String): InteractiveNotification.Node

    fun create(notificationId: Int): InteractiveNotificationFlow

    fun next()

    fun cancel()

    fun onDone(callback: Runnable)

    fun release()
}