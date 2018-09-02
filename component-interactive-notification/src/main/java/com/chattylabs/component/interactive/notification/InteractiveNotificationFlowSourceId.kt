package com.chattylabs.component.interactive.notification

interface InteractiveNotificationFlowSourceId {
    fun from(id: String): InteractiveNotificationFlowTargetId
}
