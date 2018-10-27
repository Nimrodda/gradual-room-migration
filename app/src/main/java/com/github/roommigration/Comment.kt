package com.github.roommigration

data class Comment(val id: Int,
                   val user: String,
                   val message: String) {
    companion object {
        const val TABLE_NAME = "comments"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_USER = "user"
        const val COLUMN_NAME_MESSAGE = "message"
    }
}
