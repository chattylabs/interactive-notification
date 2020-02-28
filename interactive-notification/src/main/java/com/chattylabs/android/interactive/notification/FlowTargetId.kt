package com.chattylabs.android.interactive.notification

interface FlowTargetId {
    fun to(id: String, vararg optIds: String)
}
