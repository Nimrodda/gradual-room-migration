package com.github.roommigration

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CommentDao {
    @VisibleForTesting
    @Query("SELECT * FROM ${Comment.TABLE_NAME}")
    fun testFetchAll(): List<Comment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comments: List<Comment>)
}
