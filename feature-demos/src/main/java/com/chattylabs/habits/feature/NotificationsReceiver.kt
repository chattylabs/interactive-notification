package com.chattylabs.habits.feature

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.chattylabs.component.interactive.notification.InteractiveNotification
import com.chattylabs.component.interactive.notification.InteractiveNotificationBuilder

class NotificationsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val id = InteractiveNotification.Utils.getNotificationId(intent)
        val action = InteractiveNotification.Utils.getActionId(intent)
        val isDismissed = InteractiveNotification.Utils.isDismissed(intent)

        Toast.makeText(context, "Id: $id - Action: $action - Dismissed: $isDismissed",
                Toast.LENGTH_LONG).show()

        when  {
            id == ICONS_RIGHT && action == R.id.interactive_notification_action_2 -> {
                val map = linkedMapOf(
                        R.id.interactive_notification_action_1 to "\uD83D\uDE03",
                        R.id.interactive_notification_action_2 to "\uD83D\uDE03",
                        R.id.interactive_notification_action_3 to "\uD83D\uDE03",
                        R.id.interactive_notification_action_4 to "\uD83D\uDE03",
                        R.id.interactive_notification_action_5 to "\uD83D\uDE03")

                InteractiveNotificationBuilder(
                        context, NotificationsReceiver::class.java,
                        "A random question very very very large that might not fill entirely " +
                                "and I keep writing because I need to see how it looks like, but it seems " +
                                "that I need to write even more, actually there is space enough?",
                        "Expand to view more..", map).build().show(ICONS_RIGHT)
            }
        }
    }
}
