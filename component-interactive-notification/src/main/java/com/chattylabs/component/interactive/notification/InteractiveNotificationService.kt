package com.chattylabs.component.interactive.notification

import android.app.IntentService
import android.content.Intent

class InteractiveNotificationService : IntentService("InteractiveNotificationService") {
    override fun onHandleIntent(intent: Intent?) {
        intent?.run {
            val actionId = InteractiveNotification.Utils.getActionId(this)
            val isDismissed = InteractiveNotification.Utils.isDismissed(this)
            val component: InteractiveNotificationComponentImpl =
                    InteractiveNotificationComponentImpl.Instance.get() as InteractiveNotificationComponentImpl
            if (!isDismissed) component.currentNode = component.getNode(actionId!!)
            applicationContext.sendBroadcast(
                    Intent(applicationContext, component.receiver).putExtras(extras!!))
        }
    }
}