package com.chattylabs.component.interactive.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.support.v4.util.SimpleArrayMap
import com.chattylabs.sdk.android.common.internal.ILogger
import java.lang.ref.SoftReference
import java.util.*
import javax.inject.Inject

internal class InteractiveNotificationComponentImpl :
        InteractiveNotificationFlow.Edge(), InteractiveNotificationComponent {

    lateinit var context: Context @Inject set
    lateinit var receiver: Class<out BroadcastReceiver> @Inject set

    internal lateinit var logger: ILogger @Inject set

    private val mGraph = SimpleArrayMap<InteractiveNotification.Node, ArrayList<InteractiveNotification.Node>>()

    internal lateinit var currentNode: InteractiveNotification.Node @Inject set

    private var done: Runnable? = null
    private var notificationId: Int = DEFAULT_ID

    internal object Instance {
        lateinit var instanceOf: SoftReference<InteractiveNotificationComponent>
        fun get(): InteractiveNotificationComponent {
            synchronized(Instance::class.java) {
                return if (!Instance::instanceOf.isInitialized || instanceOf.get() == null) {
                    InteractiveNotificationComponentImpl()
                } else instanceOf.get()!!
            }
        }
    }

    init {
        Instance.instanceOf = SoftReference(this)
    }

    override fun release() = mGraph.clear()
            .also { cancel() }
            .also { done = null }

    override fun create(notificationId: Int): InteractiveNotificationFlow {
        this.notificationId = notificationId
        return InteractiveNotificationFlow(this)
    }

    override fun start(node: InteractiveNotification.Node) {
        if (node is InteractiveNotification.Message) {
            currentNode = node
            show(currentNode)
        } else throw IllegalStateException("Only a Message can start the flow.")
    }

    override fun cancel() = InteractiveNotification.dismiss(context, notificationId)

    override fun next() = show(getNext())

    override fun onDone(callback: Runnable) {
        this.done = callback
    }

    override fun addEdge(node: InteractiveNotification.Node,
                         incomingEdge: InteractiveNotification.Node) {
        val originNodeExists = mGraph.containsKey(node)
        if (!originNodeExists || !mGraph.containsKey(incomingEdge)) {
            throw IllegalArgumentException("All Nodes must exist in the graph " +
                    "before to be added as an edge. The Node [" +
                    if (!originNodeExists) node.id else incomingEdge.id + "] " +
                    "does not exist in the graph. Did you forget to call addNode(Node)?"
            )
        }
        var edges: ArrayList<InteractiveNotification.Node>? = mGraph.get(node)
        if (edges == null) {
            edges = arrayListOf()
            mGraph.put(node, edges)
        }
        edges.add(incomingEdge)
    }

    override fun addNode(node: InteractiveNotification.Node) {
        if (!mGraph.containsKey(node)) {
            mGraph.put(node, null)
        }
    }

    override fun getNode(id: String): InteractiveNotification.Node {
        var i = 0
        val size = mGraph.size()
        while (i < size) {
            val node = mGraph.keyAt(i)
            if (node.id == id) {
                return node
            }
            i++
        }
        throw IllegalArgumentException("The Node [" + id + "] does not exists in the graph. " +
                "Did you forget to call addNode(Node)?")
    }

    private fun show(node: InteractiveNotification.Node?) {
        node?.also { n -> loadNotification(n) } ?: {
            cancel() // Otherwise there is no more nodes
            done?.run()
        }.invoke()
    }

    private fun loadNotification(node: InteractiveNotification.Node) {
        val outgoingEdges = getOutgoingEdges(node)
        var actions = emptyList<InteractiveNotification.Action>()
        if (outgoingEdges != null && !outgoingEdges.isEmpty()) {
            actions = getOutgoingNode(outgoingEdges) as InteractiveNotification.ActionList
            currentNode = actions
        } else currentNode = node
        InteractiveNotificationBuilder(context,
                (node as InteractiveNotification.Message).text,
                actions as InteractiveNotification.ActionList)
                .apply {
                    expandSubtitle = "Expand to view more.." // TODO
                }.build().show(notificationId)
        node.loaded?.let { it(node) }
    }

    private fun getNext(): InteractiveNotification.Node? {
        val outgoingEdges = getOutgoingEdges(currentNode)
        if (outgoingEdges == null || outgoingEdges.isEmpty()) {
            return null
        }
        return getOutgoingNode(outgoingEdges)
    }

    private fun getOutgoingNode(outgoingEdges: ArrayList<InteractiveNotification.Node>):
            InteractiveNotification.Node {
        if (outgoingEdges.size > 0) {
            if (outgoingEdges.size == 1) {
                val item = outgoingEdges[0]
                if (item is InteractiveNotification.Action) {
                    val actions = ArrayList<InteractiveNotification.Node>(1)
                    actions.add(item)
                    return getActionListFromEdges(actions)
                }
                return item
            } else {
                return getActionListFromEdges(outgoingEdges)
            }
        }
        return emptyList<InteractiveNotification.Action>() as InteractiveNotification.ActionList
    }

    private fun getActionListFromEdges(edges: ArrayList<InteractiveNotification.Node>):
            InteractiveNotification.ActionList {
        try {
            val actionList = InteractiveNotification.ActionList()
            var i = 0
            val size = edges.size
            while (i < size) {
                actionList.add(edges[i] as InteractiveNotification.Action)
                i++
            }
            return actionList.apply { sort() }
        } catch (ignored: ClassCastException) {
            throw IllegalStateException("Only actions can have multiple edges in the graph. " +
                    "Some of the followings are not Actions [${edges.joinToString { it.id }}]")
        }
    }

    private fun getIncomingEdges(node: InteractiveNotification.Node):
            ArrayList<InteractiveNotification.Node>? {
        return mGraph.get(node)
    }

    private fun getOutgoingEdges(node: InteractiveNotification.Node):
            ArrayList<InteractiveNotification.Node>? {
        var result: ArrayList<InteractiveNotification.Node>? = null
        var i = 0
        val size = mGraph.size()
        while (i < size) {
            val edges = mGraph.valueAt(i)
            if (edges != null && edges.contains(node)) {
                if (result == null) {
                    result = ArrayList()
                }
                result.add(mGraph.keyAt(i))
            }
            i++
        }
        return result
    }
}