package com.chattylabs.habits.feature

import android.os.Bundle
import android.view.View
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action
import com.chattylabs.component.interactive.notification.InteractiveNotification.Message
import com.chattylabs.component.interactive.notification.InteractiveNotificationComponent
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class DemosActivity : DaggerAppCompatActivity() {

    @Inject lateinit var component: InteractiveNotificationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) finish()
        setContentView(R.layout.activity_demos)
    }

    fun iconsRight(view: View) {
        with(component) {
            release()

            // Notification 1
            addNode(Message("1", "If you click on thumbs-up, it swaps to icons at bottom!"))
            addNode(Action("100", "\uD83D\uDC4E", 1).apply { textSize = 16f }) // 👎
            addNode(Action("101", "\uD83D\uDC4D", 0).apply { textSize = 16f }) // 👍

            // Notification 2
            addNode(Message("3", "You have previously clicked on: \uD83D\uDC4D"))
            addNode(Action("300", "\uD83E\uDD37", 0).apply { textSize = 18f })
            addNode(Action("301", "No", 1).apply { textSize = 18f })
            addNode(Action("302", "Yes", 2).apply { textSize = 18f })

            with(create(100)) {
                from("1").to("100", "101")
                from("101").to("3")
                from("3").to("300", "301", "302")
                start(getNode("1"))
            }
        }
    }

    fun iconsRightLargeTitle(view: View) {
        with(component) {
            release()

            addNode(Message("2", "A random question very very very large that might not " +
                    "even fill entirely, but I keep writing because I need to see how it looks " +
                    "like. It seems that I need to write even more. Anyway, I think there is now " +
                    "space enough, right?"))
            addNode(Action("200", "NO", 0).apply { textSize = 16f })
            addNode(Action("201", "YES", 1).apply { textSize = 16f })

            with(create(200)) {
                from("2").to("200", "201")
                start(getNode("2"))
            }
        }
    }

    fun iconsBottom(view: View) {
        with(component) {
            release()

            addNode(Action("300", "⏩", 100))           // ⏩
            addNode(Action("301", "⏩", 100))           // ⏩
            addNode(Action("350", "⏪", 0))             // ⏪
            addNode(Action("351", "⏪", 0))             // ⏪

            // Notification 1
            addNode(Message("1", "Emulate a list of items"))
            addNode(Action("311", "\uD83C\uDF4E", 1))   // 🍎
            addNode(Action("312", "\uD83C\uDF54", 2))   // 🍔
            addNode(Action("313", "\uD83D\uDE03", 3))   // 😃

            // Notification 2
            addNode(Message("2", "Emulate a list of items"))
            addNode(Action("314", "\uD83C\uDF5A", 4))   // 🍚

            // Notification 3
            addNode(Message("3", "Emulate a list of items"))
            addNode(Action("315", "\uD83E\uDD57", 5))   // 🥗
            addNode(Action("316", "\uD83E\uDD5D", 6))   // 🥝

            with(create(300)) {
                from("1").to("300", "311", "312", "313")
                from("300").to("2")
                from("2").to("301", "313", "314", "350")
                from("301").to("3")
                from("350").to("1")
                from("3").to("314", "315", "316", "351")
                from("351").to("2")
                start(getNode("1"))
            }
        }
    }

    fun iconsBottomSizes(view: View) {
        with(component) {
            release()

            addNode(Message("4", "What size do you prefer?"))
            addNode(Action("400", "\uD83C\uDF56", 0).apply { textSize = 50f })
            addNode(Action("401", "\uD83C\uDF56", 1).apply { textSize = 40f })
            addNode(Action("402", "\uD83C\uDF56", 2).apply { textSize = 30f })

            with(create(400)) {
                from("4").to("400", "401", "402")
                start(getNode("4"))
            }
        }
    }
}
