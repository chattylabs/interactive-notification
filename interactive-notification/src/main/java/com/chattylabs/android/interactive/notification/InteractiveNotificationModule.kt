package com.chattylabs.android.interactive.notification

import android.app.Application
import android.content.Context
import chattylabs.android.commons.internal.ILogger

object InteractiveNotificationModule {

    @JvmStatic
    fun provide(context: Context,
                logger: ILogger) = Graph
            .Instance.get().apply {
        (this as Graph).apply {
            this.logger = logger.apply {
                setBuildDebug(BuildConfig.DEBUG)
            }
            this.context = context as? Application ?: context.applicationContext
        }
    }
}