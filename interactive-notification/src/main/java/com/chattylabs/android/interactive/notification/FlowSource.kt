package com.chattylabs.android.interactive.notification

interface FlowSource {
    fun from(node: Node): FlowTarget
}
