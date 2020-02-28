package com.chattylabs.android.interactive.notification

interface FlowSourceId {
    fun from(id: String): FlowTargetId
}
