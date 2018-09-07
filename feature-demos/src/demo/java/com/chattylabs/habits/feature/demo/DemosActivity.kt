package com.chattylabs.habits.feature.demo

import android.os.Bundle
import android.view.View
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action
import com.chattylabs.component.interactive.notification.InteractiveNotification.Message
import com.chattylabs.component.interactive.notification.InteractiveNotificationComponent
import com.chattylabs.habits.feature.BuildConfig
import com.chattylabs.habits.feature.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class DemosActivity : DaggerAppCompatActivity() {

    @Inject lateinit var component: InteractiveNotificationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) finish()
        setContentView(R.layout.activity_demos)
        component.setReceiver(DemoNotificationsReceiver::class.java)
    }

    fun iconsRight(view: View) {
        with(component) {
            release()

            // Notification 1
            addNode(Message("1", "If you click on thumbs-up, it swaps to icons at bottom!"))
            addNode(Action("100", "\uD83D\uDC4E", 1, 16f)) // 👎
            addNode(Action("101", "\uD83D\uDC4D", 0, 16f)) // 👍

            // Notification 2
            addNode(Message("3", "You have previously clicked on: \uD83D\uDC4D"))
            addNode(Action("300", "\uD83E\uDD37", 0, 18f))
            addNode(Action("301", "No", 1, 18f))
            addNode(Action("302", "Yes", 2, 18f))

            with(prepare(100)) {
                from("1").to("100", "101")
                from("101").to("3")
                from("3").to("300", "301", "302")
                start(getNode("1"))
            }
        }
    }

    fun iconsBottom(view: View) {
        with(component) {
            release()

            addNode(Action("300", ">>", 0, 18f))               // ⏩
            addNode(Action("301", ">>", 0, 18f))               // ⏩
            addNode(Action("302", ">>", 0, 18f))               // ⏩
            addNode(Action("303", ">>", 0, 18f))               // ⏩
            addNode(Action("350", "<<", 100, 18f))             // ⏪
            addNode(Action("351", "<<", 100, 18f))             // ⏪
            addNode(Action("352", "<<", 100, 18f))             // ⏪
            addNode(Action("353", "<<", 100, 18f))             // ⏪

            addNode(Message("1", "Emulate a list of items"))
            addNode(Action("311", "\uD83C\uDF4E", 1, 18f))   // 🍎
            addNode(Action("312", "\uD83C\uDF54", 2, 18f))   // 🍔
            addNode(Action("313", "\uD83D\uDE03", 3, 18f))   // 😃

            addNode(Message("2", "Emulate a list of items"))
            addNode(Action("314", "\uD83C\uDF5A", 4, 18f))   // 🍚

            addNode(Message("3", "Emulate a list of items"))
            addNode(Action("315", "\uD83E\uDD57", 5, 18f))   // 🥗
            
            addNode(Message("4", "Emulate a list of items"))
            addNode(Action("316", "\uD83E\uDD5D", 6, 18f))   // 🥝
    
            addNode(Message("5", "Emulate a list of items"))

            with(prepare(300)) {
                from("1").to("300", "311", "312", "313")
                from("300").to("2")
                from("2").to("301", "312", "313", "350")
                from("301").to("3")
                from("350").to("1")
                from("3").to("302", "313", "314", "351")
                from("302").to("4")
                from("351").to("2")
                from("4").to("303", "314", "315", "352")
                from("303").to("5")
                from("352").to("3")
                from("5").to("314", "315", "316", "353")
                from("353").to("4")
                start(getNode("1"))
            }
        }
    }

    fun iconsBottomSizes(view: View) {
        with(component) {
            release()

            addNode(Message("4", "What size do you prefer?"))
            addNode(Action("400", "\uD83C\uDF56", 0, 50f))
            addNode(Action("401", "\uD83C\uDF56", 1, 40f))
            addNode(Action("402", "\uD83C\uDF56", 2, 30f))

            with(prepare(400)) {
                from("4").to("400", "401", "402")
                start(getNode("4"))
            }
        }
    }
}
