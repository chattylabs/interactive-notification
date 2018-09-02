package com.chattylabs.component.interactive.notification

interface InteractiveNotificationFlowTarget {
    fun to(node: InteractiveNotification.Node, vararg optNodes: InteractiveNotification.Node)
}
