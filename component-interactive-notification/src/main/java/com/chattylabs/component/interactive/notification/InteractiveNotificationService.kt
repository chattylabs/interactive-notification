package com.chattylabs.component.interactive.notification

import android.app.IntentService
import android.content.Intent
import com.chattylabs.component.interactive.notification.InteractiveNotificationComponentImpl.*
import java.util.ArrayList

class InteractiveNotificationService : IntentService("InteractiveNotificationService") {

    private val consumedActions: ArrayList<String> = arrayListOf()

    // FIXME: PASS STRUCTURE AS PARCEL!!!

    override fun onHandleIntent(intent: Intent?) {
        intent?.run {
            val actionId = InteractiveNotification.Utils.getActionId(this)
            val isDismissed = InteractiveNotification.Utils.isDismissed(this)
            val component = Instance.get() as InteractiveNotificationComponentImpl
            if (consumedActions.contains(actionId)) return
            if (!isDismissed) component.currentNode = component.getNode(actionId!!)
            applicationContext.sendBroadcast(
                    Intent(applicationContext, component.receiver).putExtras(extras!!))
            consumedActions.add(actionId!!)
        }
    }
}