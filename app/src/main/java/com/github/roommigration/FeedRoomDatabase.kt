package com.github.roommigration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val NEW_DB_NAME = "new_feed.db"

@Database(entities = [Comment::class], version = 1)
abstract class FeedRoomDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile private var INSTANCE: FeedRoomDatabase? = null

        fun getInstance(context: Context): FeedRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): FeedRoomDatabase {
            val appContext = context.applicationContext
            return Room.databaseBuilder(appContext, FeedRoomDatabase::class.java, NEW_DB_NAME)
                .build()
        }
    }
}
