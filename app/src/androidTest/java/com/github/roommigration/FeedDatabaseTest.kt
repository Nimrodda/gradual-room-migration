package com.github.roommigration

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.github.roommigration.DB_NAME
import com.github.roommigration.Feed
import com.github.roommigration.FeedDatabase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedDatabaseTest {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    @Test
    fun bootstrapDatabase() {
        appContext.deleteDatabase(DB_NAME)
        FeedDatabase(appContext).readableDatabase.use { db ->
            db.query(Feed.TABLE_NAME, null, null, null, null, null, null).use {
                assertEquals(1, it.count)
            }
        }
    }
}
