package com.chattylabs.habits.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chattylabs.component.interactive.notification.InteractiveNotification
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action
import com.chattylabs.component.interactive.notification.InteractiveNotificationBuilder

const val ICONS_RIGHT_ID = 1
const val ICONS_BOTTOM_ID = 2

class DemosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) finish()
        setContentView(R.layout.activity_demos)
    }

    fun iconsRight(view: View) {
        val actions = listOf(
                Action("100", "\uD83D\uDC4E"),
                Action("101", "\uD83D\uDC4D"))

        InteractiveNotification.dismiss(this, ICONS_RIGHT_ID)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "If you click thumbs-up emoji, it swaps to icons at bottom!", actions)
                .apply {
                    expandSubtitle = "Expand to view more.."
                }
                .build().show(ICONS_RIGHT_ID)
    }

    fun iconsRightLargeTitle(view: View) {
        val actions = listOf(
                Action("200", "NO").apply { textSize = 16f },
                Action("201", "YES").apply { textSize = 16f })

        InteractiveNotification.dismiss(this, ICONS_RIGHT_ID)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question very very very large that might not fill " +
                        "entirely and I keep writing because I need to see how it looks like, " +
                        "but it seems that I need to write even more, actually there seems to " +
                        "be space enough, right?", actions)
                .apply {
                    expandSubtitle = "Expand to view more.."
                }.build().show(ICONS_RIGHT_ID)
    }

    fun iconsBottom(view: View) {
        val actions = listOf(
                Action("300", "▶"),
                Action("311", "\uD83C\uDF4E"),
                Action("312", "\uD83C\uDF54"),
                Action("313", "\uD83D\uDE03"))

        InteractiveNotification.dismiss(this, ICONS_BOTTOM_ID)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "A random question that does nothing?", actions)
                .apply {
                    expandSubtitle = "Expand to view more.."
                }.build().show(ICONS_BOTTOM_ID)
    }

    fun iconsBottomSizes(view: View) {
        val actions = listOf(
                Action("400", "\uD83C\uDF56").apply { textSize = 20f },
                Action("401", "\uD83C\uDF56").apply { textSize = 25f },
                Action("402", "\uD83C\uDF56").apply { textSize = 30f })

        InteractiveNotification.dismiss(this, ICONS_BOTTOM_ID)
        InteractiveNotificationBuilder(
                this, NotificationsReceiver::class.java,
                "Which sizes do you like?", actions)
                .apply {
                    expandSubtitle = "Expand to view more.."
                }.build().show(ICONS_BOTTOM_ID)
    }
}
