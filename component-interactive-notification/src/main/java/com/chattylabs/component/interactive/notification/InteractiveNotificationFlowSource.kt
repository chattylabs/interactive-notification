package com.chattylabs.component.interactive.notification

interface InteractiveNotificationFlowSource {
    fun from(node: InteractiveNotification.Node): InteractiveNotificationFlowTarget
}
