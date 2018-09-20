package com.chattylabs.android.interactive.notification

interface InteractiveNotificationFlowSourceId {
    fun from(id: String): InteractiveNotificationFlowTargetId
}
