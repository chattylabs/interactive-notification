package com.chattylabs.android.interactive.notification

import android.app.IntentService
import androidx.annotation.DrawableRes

interface InteractiveNotification {
    fun setIntentService(intentService: Class<out IntentService>)

    fun addNode(node: Node)

    fun getNode(id: String): Node

    fun prepare(notificationId: Int, channelId: String, @DrawableRes icon: Int): Flow

    fun next()

    fun cancel()
}