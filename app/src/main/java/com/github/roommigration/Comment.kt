package com.github.roommigration

import android.database.Cursor
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Comment.TABLE_NAME)
data class Comment(@PrimaryKey val id: Int,
                   val user: String,
                   val message: String) {
    companion object {
        const val TABLE_NAME = "comments"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_USER = "user"
        const val COLUMN_NAME_MESSAGE = "message"

        fun fromCursor(cursor: Cursor): Comment {
            return Comment(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(Comment.COLUMN_NAME_ID)),
                message = cursor.getString(cursor.getColumnIndexOrThrow(Comment.COLUMN_NAME_MESSAGE)),
                user = cursor.getString(cursor.getColumnIndexOrThrow(Comment.COLUMN_NAME_USER))
            )
        }
    }
}
