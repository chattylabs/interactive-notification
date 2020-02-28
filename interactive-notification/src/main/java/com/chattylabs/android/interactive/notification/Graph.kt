package com.chattylabs.android.interactive.notification

import android.app.IntentService
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import chattylabs.android.commons.internal.ILogger
import com.chattylabs.android.interactive.notification.Node.*
import java.lang.ref.SoftReference
import javax.inject.Inject

internal class Graph : Flow.Edge(), InteractiveNotification {

    internal lateinit var context: Context @Inject set

    internal lateinit var logger: ILogger @Inject set

    internal var graph = HashMap<Node, ArrayList<Node>>()

    internal lateinit var currentNode: Node @Inject set

    internal lateinit var intentService: Class<out IntentService>

    internal var notificationId: Int = 0

    @IdRes internal var icon: Int = 0

    internal lateinit var channelId: String

    internal object Instance {
        lateinit var instanceOf: SoftReference<InteractiveNotification>
        fun get(): InteractiveNotification {
            synchronized(Instance::class.java) {
                return if (!Instance::instanceOf.isInitialized || instanceOf.get() == null) {
                    Graph()
                } else instanceOf.get()!!
            }
        }
    }

    init {
        Instance.instanceOf = SoftReference(this)
    }

    override fun setIntentService(intentService: Class<out IntentService>) {
        this.intentService = intentService
    }

    private fun release() = graph.clear()

    override fun prepare(notificationId: Int, channelId: String, @DrawableRes icon: Int): Flow {
        this.notificationId = notificationId
        this.channelId = channelId
        this.icon = icon
        return Flow(this)
    }

    override fun start(@NonNull node: Node) {
        if (node is Message) {
            currentNode = node
            show(currentNode)
        } else throw IllegalStateException("Only a Message can start the flow.")
    }

    override fun cancel() {
        release()
        Node.dismiss(context, notificationId)
    }

    override fun next() = show(getNext())

    override fun addEdge(node: Node,
                         incomingEdge: Node) {
        val originNodeExists = graph.containsKey(node)
        if (!originNodeExists || !graph.containsKey(incomingEdge)) {
            throw IllegalArgumentException(
                    "All Nodes must exist in the graph before to be added as an edge. The Node [" +
                            if (!originNodeExists) node.id
                            else incomingEdge.id + "] does not exist in the graph. " +
                                    "Did you forget to call addNode(Node)?"
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
        } catch (ignore: Exception) {
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

        if (outgoingEdges != null && outgoingEdges.isNotEmpty()) {
            actions = getOutgoingNode(outgoingEdges) as ActionList
            currentNode = actions
        } else currentNode = node

        NotificationMaker(context, intentService, graph, channelId, icon, node, actions)
                .show(notificationId)
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
            } else {
                return getActionListFromEdges(outgoingEdges)
            }
        }
        return emptyList<Action>() as ActionList
    }

    private fun getActionListFromEdges(edges: ArrayList<Node>): ActionList {
        try {
            val actionList = ActionList()
            var i = 0
            val size = edges.size
            while (i < size) {
                actionList.add(edges[i] as Action)
                i++
            }
            actionList.sort()
            return actionList
        } catch (ignored: ClassCastException) {
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