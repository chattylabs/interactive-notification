package com.chattylabs.android.interactive.notification

interface InteractiveNotificationFlowTarget {
    fun to(node: InteractiveNotification.Node, vararg optNodes: InteractiveNotification.Node)
}
