package com.chattylabs.habits.feature

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.chattylabs.component.interactive.notification.InteractiveNotification
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action
import com.chattylabs.component.interactive.notification.InteractiveNotificationBuilder

class NotificationsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val notificationId = InteractiveNotification.Utils.getNotificationId(intent)
        val actionId = InteractiveNotification.Utils.getActionId(intent)
        val isDismissed = InteractiveNotification.Utils.isDismissed(intent)

        if (isDismissed)
            Toast.makeText(context, "Id: $notificationId - Dismissed: $isDismissed",
                    Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, "Id: $notificationId - Action: $actionId",
                    Toast.LENGTH_LONG).show()

        when  {
            notificationId == ICONS_RIGHT_ID && actionId == "101" -> {
                val actions = listOf(
                        Action("300", "\uD83E\uDD37").apply { textSize = 18f },
                        Action("301", "No").apply { textSize = 18f },
                        Action("302", "Yes").apply { textSize = 18f })

                InteractiveNotificationBuilder(
                        context, NotificationsReceiver::class.java,
                        "You have previously clicked on: $actionId", actions)
                        .apply {
                            expandSubtitle = "Expand to view more.."
                        }.build().show(ICONS_RIGHT_ID)
            }
            // TODO: This is a test, it will be automated!
            notificationId == ICONS_BOTTOM_ID && actionId == "300" -> {
                val actions = listOf(
                        Action("300", "▶"),
                        Action("312", "\uD83C\uDF5A"),
                        Action("313", "\uD83C\uDF4E"),
                        Action("350", "◀"))

                InteractiveNotificationBuilder(
                        context, NotificationsReceiver::class.java,
                        "You have previously clicked on: $actionId", actions)
                        .apply {
                            expandSubtitle = "Expand to view more.."
                        }.build().show(ICONS_BOTTOM_ID)
            }
            //[...]
        }
    }
}
