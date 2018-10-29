package com.github.roommigration

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor

const val OLD_DB_NAME = "feed.db"
private const val DB_VER = 2

class FeedDatabase private constructor(
    private val dbExecutor: Executor,
    private val feedRoomDatabase: FeedRoomDatabase,
    context: Context
) : SQLiteOpenHelper(context, OLD_DB_NAME, null, DB_VER) {
    override fun onCreate(db: SQLiteDatabase?) {
        create(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        upgrade(dbExecutor, db, feedRoomDatabase, oldVersion, newVersion)
    }

    companion object {
        @Volatile
        private var INSTANCE: FeedDatabase? = null

        fun getInstance(
            dbExecutor: Executor,
            feedRoomDatabase: FeedRoomDatabase,
            applicationContext: Context
        ): FeedDatabase {
            return INSTANCE ?: synchronized(this) {
                FeedDatabase.INSTANCE ?: FeedDatabase(dbExecutor, feedRoomDatabase, applicationContext).also {
                    FeedDatabase.INSTANCE = it
                }
            }
        }

        @VisibleForTesting
        internal fun create(db: SQLiteDatabase?, version: Int = DB_VER) {
            db?.apply {
                // Left for testing
                if (version == 1) {
                    execSQL(SQL_CREATE_COMMENTS)
                    execSQL(SQL_BOOTSTRAP_COMMENTS)
                }

                execSQL(SQL_CREATE_FEED)
                execSQL(SQL_BOOTSTRAP_FEED)
            }
        }

        @VisibleForTesting
        internal fun upgrade(
            dbExecutor: Executor,
            db: SQLiteDatabase?,
            feedRoomDatabase: FeedRoomDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {
            if (oldVersion == 1) {
                db?.apply {
                    // Query old table
                    query(Comment.TABLE_NAME).use { cursor ->
                        if (cursor.moveToFirst()) {
                            val comments = mutableListOf<Comment>()
                            do {
                                // Map the data to Room entity and add to list
                                comments.add(Comment.fromCursor(cursor))
                            } while (cursor.moveToNext())
                            // Insert the list to the new table
                            dbExecutor.execute {
                                feedRoomDatabase.commentDao()
                                    .insert(comments)
                            }
                        }
                    }
                    // Finally, we drop the old table
                    execSQL(SQL_DROP_COMMENTS)
                }
            }
        }
    }
}

fun SQLiteDatabase.query(tableName: String): Cursor = query(tableName, null, null, null, null, null, null)

private const val SQL_CREATE_FEED = """
    CREATE TABLE ${Feed.TABLE_NAME} (
            ${Feed.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
            ${Feed.COLUMN_NAME_TITLE} TEXT,
            ${Feed.COLUMN_NAME_SUBTITLE} TEXT)
"""

private const val SQL_BOOTSTRAP_FEED = """
    INSERT INTO ${Feed.TABLE_NAME} VALUES (1, "Foo", "Bar")
"""

private const val SQL_DROP_COMMENTS = """
    DROP TABLE IF EXISTS ${Comment.TABLE_NAME}
"""

private const val SQL_CREATE_COMMENTS = """
    CREATE TABLE IF NOT EXISTS ${Comment.TABLE_NAME} (
            ${Comment.COLUMN_NAME_ID} INTEGER NOT NULL,
            ${Comment.COLUMN_NAME_USER} TEXT NOT NULL,
            ${Comment.COLUMN_NAME_MESSAGE} TEXT NOT NULL,
            PRIMARY KEY(${Comment.COLUMN_NAME_ID})
    )
"""

private const val SQL_BOOTSTRAP_COMMENTS = """
    INSERT INTO ${Comment.TABLE_NAME} VALUES (100, "Foo", "Bar")
"""
