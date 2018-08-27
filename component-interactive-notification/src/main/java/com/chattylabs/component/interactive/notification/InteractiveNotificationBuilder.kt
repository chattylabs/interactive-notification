package com.chattylabs.component.interactive.notification

import android.content.BroadcastReceiver
import android.content.Context
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action

class InteractiveNotificationBuilder(
        private val context: Context,
        private val receiver: Class<out BroadcastReceiver>,
        private val contentTitle: CharSequence,
        private val actions: List<Action>) {

    init {
        //TODO: check if actions.key is a resource id
    }

    var expandSubtitle: CharSequence? = null

    fun build(): InteractiveNotification = InteractiveNotificationImpl(
            context, receiver, contentTitle, expandSubtitle, actions)
}
