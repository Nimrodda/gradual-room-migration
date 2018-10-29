package com.github.roommigration

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedDatabaseTest {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @JvmField
    @Rule
    var migrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        FeedRoomDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration1to2DataCopiedSuccessfullyToNewDatabase() {
        val newDb = Room.inMemoryDatabaseBuilder(appContext, FeedRoomDatabase::class.java).build()

        // Create old database in version 1 and then apply migration to version 2
        val oldDb = SQLiteDatabase.create(null)
        FeedDatabase.create(oldDb, 1)
        FeedDatabase.upgrade(SynchronousExecutor(), oldDb, newDb, 1, 2)

        // First indication that our migration was successful - comments table was dropped
        assertFalse(oldDb.checkTableExists(Comment.TABLE_NAME))
        oldDb.close()

        // Second indication that our migration was successful - we have the data in the new database
        newDb.apply {
            val comments = commentDao().testFetchAll()
            assertEquals(1, comments.size)
            assertEquals(100, comments.first().id)
            assertEquals("Foo", comments.first().user)
            assertEquals("Bar", comments.first().message)
        }
    }
}

fun SQLiteDatabase.checkTableExists(tableName: String): Boolean {
    rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = ?", arrayOf(tableName)).use { cursor ->
        return cursor.count > 0
    }
}
