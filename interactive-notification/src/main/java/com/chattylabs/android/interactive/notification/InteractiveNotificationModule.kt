package com.chattylabs.android.interactive.notification

import android.app.Application
import android.content.Context
import com.chattylabs.android.commons.internal.ILogger

object InteractiveNotificationModule {

    @JvmStatic
    fun provideComponent(context: Context,
                         logger: ILogger) = InteractiveNotificationComponentImpl
            .Instance.get().apply {
        (this as InteractiveNotificationComponentImpl).apply {
            this.logger = logger.apply {
                setBuildDebug(BuildConfig.DEBUG)
            }
            this.context = context as? Application ?: context.applicationContext
        }
    }
}