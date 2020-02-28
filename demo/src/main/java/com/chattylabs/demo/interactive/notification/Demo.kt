package com.chattylabs.demo.interactive.notification

import android.content.Context
import chattylabs.android.commons.internal.ILogger
import chattylabs.android.commons.internal.ILoggerImpl
import com.chattylabs.android.interactive.notification.InteractiveNotification
import com.chattylabs.android.interactive.notification.InteractiveNotificationModule
import dagger.Binds
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

class Demo : DaggerApplication() {

    @dagger.Component(modules = [
        AndroidSupportInjectionModule::class, ApplicationModule::class])
    @Singleton
    interface ApplicationComponent : AndroidInjector<Demo> {
        @dagger.Component.Builder
        abstract class Builder : AndroidInjector.Builder<Demo>() {
            @dagger.BindsInstance
            internal abstract fun context(context: Context): Builder
        }
    }

    @dagger.Module
    abstract class ApplicationModule {

        @dagger.Module
        companion object {

            @JvmStatic
            @dagger.Provides
            @Singleton
            fun provideInteractiveNotification(context: Context, logger: ILogger):
                    InteractiveNotification = InteractiveNotificationModule.provide(context, logger)
        }

        @Binds
        abstract fun logger(logger: ILoggerImpl): ILogger
    
        @ContributesAndroidInjector
        abstract fun demoActivity(): DemoActivity
    }

    override fun applicationInjector(): AndroidInjector<out Demo> {
        return DaggerDemo_ApplicationComponent.builder().context(this).create(this)
    }
}