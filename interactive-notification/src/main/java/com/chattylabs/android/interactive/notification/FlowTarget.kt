package com.chattylabs.android.interactive.notification

interface FlowTarget {
    fun to(node: Node, vararg optNodes: Node)
}
