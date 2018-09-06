package com.chattylabs.component.interactive.notification

import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import com.chattylabs.component.interactive.notification.InteractiveNotificationComponentImpl.Instance
import java.util.*

class InteractiveNotificationService : IntentService("InteractiveNotificationService") {

    private val consumedActions: ArrayList<String> = arrayListOf()

    // FIXME: PASS STRUCTURE AS PARCEL!!!

    override fun onHandleIntent(intent: Intent?) {
        intent?.run {
            val receiver = InteractiveNotification.Utils.getReceiverClass(this)
            val messageId = InteractiveNotification.Utils.getMessageId(this)
            val actionId = InteractiveNotification.Utils.getActionId(this)
            val isDismissed = InteractiveNotification.Utils.isDismissed(this)
            val component = Instance.get() as InteractiveNotificationComponentImpl
            if (actionId == null || consumedActions.contains(actionId)) return
            consumedActions.add(actionId)
            if (BuildConfig.DEBUG)
                Toast.makeText(applicationContext,
                        "Reached action: $messageId.$actionId", Toast.LENGTH_LONG).show()
            if (!isDismissed) {
                component.currentNode = component.getNode("$messageId.$actionId")
                component.next()
            } else component.release()
            applicationContext.sendBroadcast(
                    Intent(applicationContext, receiver)
                            .setFlags(Intent.FLAG_RECEIVER_FOREGROUND).putExtras(extras!!))
        }
    }
}