package com.chattylabs.android.interactive.notification

interface InteractiveNotificationFlowTargetId {
    fun to(id: String, vararg optIds: String)
}
