package com.chattylabs.component.interactive.notification

interface InteractiveNotificationFlowTargetId {
    fun to(id: String, vararg optIds: String)
}
