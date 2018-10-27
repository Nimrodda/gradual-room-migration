package com.github.roommigration

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_NAME = "feed.db"
private const val DB_VER = 1

class FeedDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_FEED)
        db?.execSQL(SQL_BOOTSTRAP_FEED)
        db?.execSQL(SQL_CREATE_COMMENTS)
        db?.execSQL(SQL_BOOTSTRAP_COMMENTS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

private const val SQL_CREATE_FEED = """
    CREATE TABLE ${Feed.TABLE_NAME} (
            ${Feed.COLUMN_NAME_ID} INTEGER PRIMARY KEY,
            ${Feed.COLUMN_NAME_TITLE} TEXT,
            ${Feed.COLUMN_NAME_SUBTITLE} TEXT)
"""

private const val SQL_BOOTSTRAP_FEED = """
    INSERT INTO ${Feed.TABLE_NAME} VALUES (1, "Foo", "Bar")
"""

private const val SQL_CREATE_COMMENTS = """
    CREATE TABLE ${Comment.TABLE_NAME} (
            ${Comment.COLUMN_NAME_ID} INTEGER NOT NULL,
            ${Comment.COLUMN_NAME_USER} TEXT NOT NULL,
            ${Comment.COLUMN_NAME_MESSAGE} TEXT NOT NULL,
            PRIMARY KEY(${Comment.COLUMN_NAME_ID})
    )
"""

private const val SQL_BOOTSTRAP_COMMENTS = """
    INSERT INTO ${Comment.TABLE_NAME} VALUES (1, "Foo", "Bar")
"""
