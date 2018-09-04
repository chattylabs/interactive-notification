package com.chattylabs.component.interactive.notification

import android.content.BroadcastReceiver

const val DEFAULT_ID = 1

interface InteractiveNotificationComponent {
    fun setReceiver(receiver: Class<out BroadcastReceiver>)

    fun addNode(node: InteractiveNotification.Node)

    fun getNode(id: String): InteractiveNotification.Node

    fun create(notificationId: Int): InteractiveNotificationFlow

    fun next()

    fun cancel()

    fun onDone(callback: Runnable)

    fun release()
}