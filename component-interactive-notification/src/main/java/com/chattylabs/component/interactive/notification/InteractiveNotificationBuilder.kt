package com.chattylabs.component.interactive.notification

import android.content.Context
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action

internal class InteractiveNotificationBuilder(
        private val context: Context,
        private val contentTitle: CharSequence,
        private val actions: List<Action>) {

    var expandSubtitle: CharSequence? = null

    fun build(): InteractiveNotification = InteractiveNotificationImpl(
            context, contentTitle, expandSubtitle, actions)
}
