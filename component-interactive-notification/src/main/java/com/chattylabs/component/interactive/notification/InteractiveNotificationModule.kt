package com.chattylabs.component.interactive.notification

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import com.chattylabs.habits.component.core.BuildConfig
import com.chattylabs.sdk.android.common.internal.ILogger

object InteractiveNotificationModule {

    @JvmStatic
    fun provideComponent(context: Context,
                         receiver: Class<out BroadcastReceiver>,
                         logger: ILogger) = InteractiveNotificationComponentImpl
            .Instance.get().apply {
        (this as InteractiveNotificationComponentImpl).apply {
            this.logger = logger.apply {
                setBuildDebug(BuildConfig.DEBUG)
            }
            this.context = context as? Application ?: context.applicationContext
            this.receiver = receiver
        }
    }
}