package com.chattylabs.habits.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chattylabs.component.interactive.notification.InteractiveNotificationBuilder

class DemosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) finish()
        setContentView(R.layout.activity_demos)
    }

    fun open2IconsNotification(view: View) {
        val map = linkedMapOf(
                R.id.interactive_notification_action_1 to "\uD83D\uDC4D",
                R.id.interactive_notification_action_2 to "\uD83D\uDC4E")

        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question?", map).build().show()
    }

    fun openIconsNotificationAtBottom(view: View) {
        val map = linkedMapOf(
                R.id.interactive_notification_action_1 to "\uD83D\uDE03",
                R.id.interactive_notification_action_2 to "\uD83D\uDE03",
                R.id.interactive_notification_action_3 to "\uD83D\uDE03",
                R.id.interactive_notification_action_4 to "\uD83D\uDE03",
                R.id.interactive_notification_action_5 to "\uD83D\uDE03")

        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question?", map).build().show()
    }
}
