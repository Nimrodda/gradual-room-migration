package com.github.roommigration

data class Feed(
    val id: Int,
    val title: String,
    val subtitle: String
) {
    companion object {
        const val TABLE_NAME = "feed"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_SUBTITLE = "subtitle"
    }
}
