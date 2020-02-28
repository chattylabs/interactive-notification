package com.chattylabs.android.interactive.notification

class Flow internal constructor(private val edge: Edge) :
        FlowSource, FlowSourceId {
    private var from: Node? = null

    private val target = object : FlowTarget {
        override fun to(node: Node,
                        vararg optNodes: Node) {
            edge.addEdge(node, from!!)
            for (n in optNodes) edge.addEdge(n, from!!)
        }
    }

    private val targetId = object : FlowTargetId {
        override fun to(id: String, vararg optIds: String) {
            edge.addEdge(edge.getNode(id), from!!)
            for (s in optIds) edge.addEdge(edge.getNode(s), from!!)
        }
    }

    override fun from(node: Node): FlowTarget {
        from = node
        return target
    }

    override fun from(id: String): FlowTargetId {
        from = edge.getNode(id)
        return targetId
    }

    fun start(node: Node) = edge.start(node)

    internal abstract class Edge {
        internal abstract fun getNode(id: String): Node
        internal abstract fun addEdge(node: Node,
                                      incomingEdge: Node)
        internal abstract fun start(node: Node)
    }
}
