package com.chattylabs.component.interactive.notification

import android.content.BroadcastReceiver
import android.content.Context
import com.chattylabs.android.commons.internal.ILogger
import com.chattylabs.component.interactive.notification.InteractiveNotification.*
import java.lang.ref.SoftReference
import javax.inject.Inject

internal class InteractiveNotificationComponentImpl :
        InteractiveNotificationFlow.Edge(), InteractiveNotificationComponent {
    
    lateinit var context: Context @Inject set
    
    internal lateinit var logger: ILogger @Inject set
    
    internal var graph = HashMap<Node, ArrayList<Node>>()
    
    internal lateinit var currentNode: Node @Inject set
    
    internal var notificationId: Int = DEFAULT_ID
    
    internal object Instance {
        lateinit var instanceOf: SoftReference<InteractiveNotificationComponent>
        fun get(): InteractiveNotificationComponent {
            synchronized(Instance::class.java) {
                return if (!Instance::instanceOf.isInitialized || instanceOf.get() == null) {
                    InteractiveNotificationComponentImpl()
                }
                else instanceOf.get()!!
            }
        }
    }
    
    init {
        Instance.instanceOf = SoftReference(this)
    }
    
    private lateinit var receiver: Class<out BroadcastReceiver>
    
    override fun setReceiver(receiver: Class<out BroadcastReceiver>) {
        this.receiver = receiver
    }
    
    private fun release() = graph.clear()
    
    override fun prepare(notificationId: Int): InteractiveNotificationFlow {
        this.notificationId = notificationId
        return InteractiveNotificationFlow(this)
    }
    
    override fun start(node: Node) {
        if (node is Message) {
            currentNode = node
            show(currentNode)
        }
        else throw IllegalStateException("Only a Message can start the flow.")
    }
    
    override fun cancel() {
        release()
        InteractiveNotification.dismiss(context, notificationId)
    }
    
    override fun next() = show(getNext())
    
    override fun addEdge(node: Node,
                         incomingEdge: Node) {
        val originNodeExists = graph.containsKey(node)
        if (!originNodeExists || !graph.containsKey(incomingEdge)) {
            throw IllegalArgumentException("All Nodes must exist in the graph " +
                                           "before to be added as an edge. The Node [" +
                                           if (!originNodeExists) node.id
                                           else incomingEdge.id + "] " +
                                                "does not exist in the graph. Did you forget to call addNode(Node)?"
            )
        }
        var edges: ArrayList<Node>? = graph[node]
        if (edges == null) {
            edges = arrayListOf()
            graph[node] = edges
        }
        edges.add(incomingEdge)
    }
    
    override fun addNode(node: Node) {
        if (!graph.containsKey(node)) {
            graph[node] = arrayListOf()
        }
    }
    
    override fun getNode(id: String): Node {
        try {
            return graph.filterKeys { it.id == id }.keys.first()
        }
        catch (ignore: Exception) {
            throw IllegalArgumentException("The Node [" + id + "] does not exists in the graph. " +
                                           "Did you forget to call addNode(Node)?")
        }
    }
    
    private fun show(node: Node?) {
        node?.also { n -> loadNotification(n) } ?: {
            cancel() // Otherwise there is no more nodes
        }.invoke()
    }
    
    private fun loadNotification(node: Node) {
        val outgoingEdges = getOutgoingEdges(node)
        var actions = emptyList<Action>()
        if (outgoingEdges != null && !outgoingEdges.isEmpty()) {
            actions = getOutgoingNode(outgoingEdges) as InteractiveNotification.ActionList
            currentNode = actions
        }
        else currentNode = node
        InteractiveNotificationBuilder(context, graph, node, receiver,
                if (actions.isEmpty()) actions else actions as InteractiveNotification.ActionList)
                .apply {
                    expandSubtitle = "Expand to view more.." // TODO
                }.build().show(notificationId)
    }
    
    private fun getNext(): Node? {
        val outgoingEdges = getOutgoingEdges(currentNode)
        if (outgoingEdges == null || outgoingEdges.isEmpty()) {
            return null
        }
        return getOutgoingNode(outgoingEdges)
    }
    
    private fun getOutgoingNode(outgoingEdges: ArrayList<Node>): Node {
        if (outgoingEdges.size > 0) {
            if (outgoingEdges.size == 1) {
                val item = outgoingEdges[0]
                if (item is Action) {
                    val actions = ArrayList<Node>(1)
                    actions.add(item)
                    return getActionListFromEdges(actions)
                }
                return item
            }
            else {
                return getActionListFromEdges(outgoingEdges)
            }
        }
        return emptyList<Action>() as InteractiveNotification.ActionList
    }
    
    private fun getActionListFromEdges(edges: ArrayList<Node>): InteractiveNotification.ActionList {
        try {
            val actionList = InteractiveNotification.ActionList()
            var i = 0
            val size = edges.size
            while (i < size) {
                actionList.add(edges[i] as Action)
                i++
            }
            actionList.sort()
            return actionList
        }
        catch (ignored: ClassCastException) {
            throw IllegalStateException("Only actions can have multiple edges in the graph. " +
                                        "Some of the followings are not Actions [${edges.joinToString { it.id }}]")
        }
    }
    
    private fun getIncomingEdges(node: Node): ArrayList<Node>? {
        return graph[node]
    }
    
    private fun getOutgoingEdges(node: Node): ArrayList<Node>? {
        return graph.filter { it.value.contains(node) }.keys.toCollection(arrayListOf())
    }
}