package com.chattylabs.component.interactive.notification

import com.chattylabs.habits.component.core.BuildConfig
import com.chattylabs.sdk.android.common.internal.ILogger

@dagger.Module
object InteractiveNotificationModule {

    @dagger.Provides
    @dagger.Reusable
    @JvmStatic fun provideComponent(logger: ILogger) = InteractiveNotificationComponentImpl
            .Instance.get().apply {
        (this as InteractiveNotificationComponentImpl).setLogger(logger.apply {
            setBuildDebug(BuildConfig.DEBUG) }) }
}