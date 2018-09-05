package com.chattylabs.habits.feature

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.chattylabs.component.interactive.notification.InteractiveNotification
import com.chattylabs.component.interactive.notification.InteractiveNotificationComponent
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject

class DemoNotificationsReceiver : DaggerBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val notificationId = InteractiveNotification.Utils.getNotificationId(intent)
        val actionId = InteractiveNotification.Utils.getActionId(intent)
        val isDismissed = InteractiveNotification.Utils.isDismissed(intent)

        if (isDismissed)
            Toast.makeText(context, "Id: $notificationId - Dismissed: $isDismissed",
                    Toast.LENGTH_LONG).show()
        else {
            Toast.makeText(context, "Id: $notificationId - Action: $actionId",
                    Toast.LENGTH_LONG).show()
        }
    }
}
