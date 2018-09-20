package com.chattylabs.android.interactive.notification

interface InteractiveNotificationFlowSource {
    fun from(node: InteractiveNotification.Node): InteractiveNotificationFlowTarget
}
