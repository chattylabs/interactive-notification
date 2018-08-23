package com.chattylabs.component.interactive.notification

import android.os.Bundle

interface InteractiveNotification {

    class Utils {
        companion object {
            fun getActionId(bundle: Bundle): Int = bundle.getInt(ACTION_ID)
        }
    }

    fun show()
    fun dismiss()
}

internal const val ACTION_ID = "action_id"