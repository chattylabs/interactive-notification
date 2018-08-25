package com.chattylabs.habits.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chattylabs.component.interactive.notification.InteractiveNotification
import com.chattylabs.component.interactive.notification.InteractiveNotificationBuilder

const val ICONS_RIGHT = 1
const val ICONS_BOTTOM = 2

class DemosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) finish()
        setContentView(R.layout.activity_demos)
    }

    fun open2IconsRight_1(view: View) {
        val map = linkedMapOf(
                R.id.interactive_notification_action_1 to "\uD83D\uDC4D",
                R.id.interactive_notification_action_2 to "\uD83D\uDC4E")

        InteractiveNotification.dismiss(this, ICONS_RIGHT)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question?",
                "Expand to view more..", map).build().show(ICONS_RIGHT)
    }

    fun open2IconsRight_2(view: View) {
        val map = linkedMapOf(
                R.id.interactive_notification_action_1 to "\uD83D\uDC4D",
                R.id.interactive_notification_action_2 to "\uD83D\uDC4E")

        InteractiveNotification.dismiss(this, ICONS_RIGHT)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question very very very large that might not fill entirely " +
                        "and I keep writing because I need to see how it looks like, but it seems " +
                        "that I need to write even more, actually there is space enough?",
                "Expand to view more..", map).build().show(ICONS_RIGHT)
    }

    fun openIconsBottom_1(view: View) {
        val map = linkedMapOf(
                R.id.interactive_notification_action_1 to "\uD83D\uDE03",
                R.id.interactive_notification_action_2 to "\uD83D\uDE03",
                R.id.interactive_notification_action_3 to "\uD83D\uDE03",
                R.id.interactive_notification_action_4 to "\uD83D\uDE03",
                R.id.interactive_notification_action_5 to "\uD83D\uDE03")

        InteractiveNotification.dismiss(this, ICONS_BOTTOM)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question?",
                "Expand to view more..", map).build().show(ICONS_BOTTOM)
    }

    fun openIconsBottom_2(view: View) {
        val map = linkedMapOf(
                R.id.interactive_notification_action_1 to "\uD83D\uDE03",
                R.id.interactive_notification_action_2 to "\uD83D\uDE03",
                R.id.interactive_notification_action_3 to "\uD83D\uDE03",
                R.id.interactive_notification_action_4 to "\uD83D\uDE03",
                R.id.interactive_notification_action_5 to "\uD83D\uDE03")

        InteractiveNotification.dismiss(this, ICONS_BOTTOM)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question very very very large that might not fill entirely " +
                        "and I keep writing because I need to see how it looks like, but it seems " +
                        "that I need to write even more, actually there is space enough?",
                "Expand to view more..", map).build().show(ICONS_BOTTOM)
    }
}
