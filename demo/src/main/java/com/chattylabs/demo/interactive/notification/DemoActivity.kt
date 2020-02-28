package com.chattylabs.demo.interactive.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import com.chattylabs.android.interactive.notification.InteractiveNotification
import com.chattylabs.android.interactive.notification.Node
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class DemoActivity : DaggerAppCompatActivity() {

    @Inject lateinit var component: InteractiveNotification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG) finish()
        setContentView(R.layout.activity_demo)

        val notificationManager: NotificationManager = applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(packageName, "Testing Notifications", NotificationManager.IMPORTANCE_HIGH))
        }

        component.setIntentService(DemoNotificationsService::class.java)
    }

    /*
     * This function is fired inside [R.layout.activity_demo].
     *
     * It demonstrates a notification with 2 actions at right.
     */
    fun iconsRight(view: View) {

        with(component) {
            cancel()

            // Notification 1
            addNode(Node.Message("1", "If you click on thumbs-up, it swaps to icons at bottom!"))
            addNode(Node.Action("100", "\uD83D\uDC4E", 1, 16f)) // 👎
            addNode(Node.Action("101", "\uD83D\uDC4D", 0, 16f)) // 👍

            // Notification 2
            addNode(Node.Message("3", "You have previously clicked on: \uD83D\uDC4D"))
            addNode(Node.Action("300", "\uD83E\uDD37", 0, 18f))
            addNode(Node.Action("301", "No", 1, 18f))
            addNode(Node.Action("302", "Yes", 2, 18f))

            with(prepare(100, packageName, R.mipmap.ic_launcher)) {
                from("1").to("100", "101")
                from("101").to("3")
                from("3").to("300", "301", "302")
                start(getNode("1"))
            }
        }
    }
    
    /*
     * This function is fired inside [R.layout.activity_demo].
     *
     * It demonstrates a notification with actions at bottom.
     *
     * This is a particular proof of concept where you can have a list of
     * multiple actions horizontally.
     */
    fun iconsBottom(view: View) {
        with(component) {
            cancel()

            addNode(Node.Action("300", ">>", 0, 18f))               // ⏩
            addNode(Node.Action("301", ">>", 0, 18f))               // ⏩
            addNode(Node.Action("302", ">>", 0, 18f))               // ⏩
            addNode(Node.Action("303", ">>", 0, 18f))               // ⏩
            addNode(Node.Action("350", "<<", 100, 18f))             // ⏪
            addNode(Node.Action("351", "<<", 100, 18f))             // ⏪
            addNode(Node.Action("352", "<<", 100, 18f))             // ⏪
            addNode(Node.Action("353", "<<", 100, 18f))             // ⏪

            addNode(Node.Message("1", "Emulates a horizontal list of items"))
            addNode(Node.Action("311", "\uD83C\uDF4E", 1, 18f))   // 🍎
            addNode(Node.Action("312", "\uD83C\uDF54", 2, 18f))   // 🍔
            addNode(Node.Action("313", "\uD83D\uDE03", 3, 18f))   // 😃

            addNode(Node.Message("2", "Emulates a horizontal list of items"))
            addNode(Node.Action("314", "\uD83C\uDF5A", 4, 18f))   // 🍚

            addNode(Node.Message("3", "Emulates a horizontal list of items"))
            addNode(Node.Action("315", "\uD83E\uDD57", 5, 18f))   // 🥗
            
            addNode(Node.Message("4", "Emulates a horizontal list of items"))
            addNode(Node.Action("316", "\uD83E\uDD5D", 6, 18f))   // 🥝
    
            addNode(Node.Message("5", "Emulates a horizontal list of items"))

            with(prepare(300, packageName, R.mipmap.ic_launcher)) {
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
    
    /*
     * This function is fired inside [R.layout.activity_demo].
     *
     * It demonstrates a notification with different action sizes at bottom.
     */
    fun iconsBottomSizes(view: View) {
        with(component) {
            cancel()

            addNode(Node.Message("4", "What size do you prefer?"))
            addNode(Node.Action("400", "\uD83C\uDF56", 0, 50f))
            addNode(Node.Action("401", "\uD83C\uDF56", 1, 40f))
            addNode(Node.Action("402", "\uD83C\uDF56", 2, 30f))

            with(prepare(400, packageName, R.mipmap.ic_launcher)) {
                from("4").to("400", "401", "402")
                start(getNode("4"))
            }
        }
    }
}
