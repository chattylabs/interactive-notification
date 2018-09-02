package com.chattylabs.component.interactive.notification

class InteractiveNotificationFlow internal constructor(private val edge: Edge) :
        InteractiveNotificationFlowSource, InteractiveNotificationFlowSourceId {
    private var from: InteractiveNotification.Node? = null

    private val target = object : InteractiveNotificationFlowTarget {
        override fun to(node: InteractiveNotification.Node,
                        vararg optNodes: InteractiveNotification.Node) {
            edge.addEdge(node, from!!)
            for (n in optNodes) edge.addEdge(n, from!!)
        }
    }

    private val targetId = object : InteractiveNotificationFlowTargetId {
        override fun to(id: String, vararg optIds: String) {
            edge.addEdge(edge.getNode(id), from!!)
            for (s in optIds) edge.addEdge(edge.getNode(s), from!!)
        }
    }

    override fun from(node: InteractiveNotification.Node): InteractiveNotificationFlowTarget {
        from = node
        return target
    }

    override fun from(id: String): InteractiveNotificationFlowTargetId {
        from = edge.getNode(id)
        return targetId
    }

    fun start(node: InteractiveNotification.Node) = edge.start(node)

    internal abstract class Edge {
        internal abstract fun getNode(id: String): InteractiveNotification.Node
        internal abstract fun addEdge(node: InteractiveNotification.Node,
                                      incomingEdge: InteractiveNotification.Node)
        internal abstract fun start(node: InteractiveNotification.Node)
    }
}
