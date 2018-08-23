package com.chattylabs.habits.feature

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.chattylabs.component.interactive.notification.InteractiveNotification

class NotificationsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val action = InteractiveNotification.Utils.getActionId(intent.extras)
        Toast.makeText(context, "Action: $action", Toast.LENGTH_LONG).show()
    }
}
