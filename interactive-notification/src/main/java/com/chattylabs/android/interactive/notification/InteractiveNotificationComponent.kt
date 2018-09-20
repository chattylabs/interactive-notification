package com.chattylabs.android.interactive.notification

import android.content.BroadcastReceiver

const val DEFAULT_ID = 1

interface InteractiveNotificationComponent {
    fun setReceiver(receiver: Class<out BroadcastReceiver>)

    fun addNode(node: InteractiveNotification.Node)

    fun getNode(id: String): InteractiveNotification.Node

    fun prepare(notificationId: Int): InteractiveNotificationFlow

    fun next()

    fun cancel()
}