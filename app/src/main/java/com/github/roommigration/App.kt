package com.github.roommigration

import android.app.Application
import java.util.concurrent.Executors

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val dbExecutor = Executors.newSingleThreadExecutor()
        // By accessing the old database we make sure that the migrations are executed
        FeedDatabase.getInstance(dbExecutor, FeedRoomDatabase.getInstance(this), this).readableDatabase
    }
}
