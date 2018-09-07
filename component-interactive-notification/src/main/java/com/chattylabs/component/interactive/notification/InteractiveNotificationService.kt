package com.chattylabs.component.interactive.notification

import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import com.chattylabs.component.interactive.notification.InteractiveNotification.Node
import com.chattylabs.component.interactive.notification.InteractiveNotification.Utils
import com.chattylabs.component.interactive.notification.InteractiveNotificationComponentImpl.Instance

class InteractiveNotificationService : IntentService("InteractiveNotificationService") {

    private val consumedActions: ArrayList<String> = arrayListOf()

    // FIXME: PASS STRUCTURE AS PARCEL!!!

    override fun onHandleIntent(intent: Intent?) {
        intent?.run {
            val graph = Utils.getGraph(this)
            val notificationId = Utils.getNotificationId(this)
            val receiver = Utils.getReceiverClass(this)
            val messageId = Utils.getMessageId(this)
            val actionId = Utils.getActionId(this)
            val isDismissed = Utils.isDismissed(this)
            val component = Instance.get() as InteractiveNotificationComponentImpl
            if (actionId == null || consumedActions.contains(actionId)) return
            consumedActions.add(actionId)
            if (BuildConfig.DEBUG)
                Toast.makeText(applicationContext,
                        "Reached action: $messageId.$actionId", Toast.LENGTH_LONG).show()
            if (!isDismissed) {
                try {
                    component.currentNode = component.getNode(actionId)
                    if (BuildConfig.DEBUG) print("Used component.currentNode")
                } catch (ignore: Exception) {
                    @Suppress("UNCHECKED_CAST")
                    component.graph =  graph as HashMap<Node, ArrayList<Node>>
                    component.notificationId = notificationId
                    component.currentNode = component.getNode(actionId)
                }
                component.next()
            } else component.release()
            applicationContext.sendBroadcast(
                    Intent(applicationContext, receiver)
                            .setFlags(Intent.FLAG_RECEIVER_FOREGROUND).putExtras(extras!!))
        }
    }
}