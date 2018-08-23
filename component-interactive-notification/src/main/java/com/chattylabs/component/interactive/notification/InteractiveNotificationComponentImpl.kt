package com.chattylabs.component.interactive.notification

import com.chattylabs.sdk.android.common.internal.ILogger
import java.lang.ref.SoftReference

internal class InteractiveNotificationComponentImpl : InteractiveNotificationComponent {

    // Log stuff
    private var logger: ILogger? = null

    internal object Instance {
        var instanceOf: SoftReference<InteractiveNotificationComponent>? = null
        fun get(): InteractiveNotificationComponent? {
            synchronized(Instance::class.java) {
                return if (instanceOf == null || instanceOf!!.get() == null) {
                    InteractiveNotificationComponentImpl()
                } else instanceOf!!.get()
            }
        }
    }

    init {
        Instance.instanceOf = SoftReference(this)
    }

    fun setLogger(logger: ILogger) {
        this.logger = logger
    }


}