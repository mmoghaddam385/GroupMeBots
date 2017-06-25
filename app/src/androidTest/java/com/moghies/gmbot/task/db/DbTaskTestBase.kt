package com.moghies.gmbot.task.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

/**
 * Created by mmogh on 6/25/2017.
 */
abstract class DbTaskTestBase {

    companion object {
        protected const val TEST_DB_NAME = "TEST_DB"
    }

    protected lateinit var dbHelper: SQLiteOpenHelper

    @Before
    open fun init() {
        BotDbHelper.Factory.DB_NAME = TEST_DB_NAME
        dbHelper = BotDbHelper.Factory.get(InstrumentationRegistry.getTargetContext())
    }

    @After
    open fun cleanup() {
        dbHelper.close()
        InstrumentationRegistry.getTargetContext().deleteDatabase(TEST_DB_NAME)
    }

    protected fun selectBot(id: String) : BotDbContract.BotsTable.BotEntry? {
        dbHelper.readableDatabase.use { db ->
            val cursor = db.query(BotDbContract.BotsTable.TABLE_NAME, null, "${BotDbContract.BotsTable.ID_COLUMN} = ?", arrayOf(id), null, null, null, null)

            if (!cursor.moveToFirst()) {
                return null
            }

            return extractBotFromCursor(cursor)
        }
    }

    protected fun assertBotExists(bot: BotDbContract.BotsTable.BotEntry) {
        val dbBot = selectBot(bot.id)

        assertNotNull(dbBot)

        dbBot?.let {
            assertEquals(bot.id, dbBot.id)
            assertEquals(bot.name, dbBot.name)
            assertEquals(bot.groupId, dbBot.groupId)
            assertEquals(bot.groupName, dbBot.groupName)
            assertEquals(bot.avatarUrl, dbBot.avatarUrl)
            assertEquals(bot.source, dbBot.source)
        }
    }

    protected fun getContentValues(bot: BotDbContract.BotsTable.BotEntry) : ContentValues {
        val values = ContentValues()

        values.put(BotDbContract.BotsTable.ID_COLUMN, bot.id)
        values.put(BotDbContract.BotsTable.SOURCE_COLUMN, bot.source)

        if (bot.name      != null) values.put(BotDbContract.BotsTable.NAME_COLUMN,       bot.name)
        if (bot.groupId   != null) values.put(BotDbContract.BotsTable.GROUP_ID_COLUMN,   bot.groupId)
        if (bot.groupName != null) values.put(BotDbContract.BotsTable.GROUP_NAME_COLUMN, bot.groupName)
        if (bot.avatarUrl != null) values.put(BotDbContract.BotsTable.AVATAR_URL_COLUMN, bot.avatarUrl)

        return values
    }

    protected fun extractBotFromCursor(cursor: Cursor) : BotDbContract.BotsTable.BotEntry {
        val idColIndex        = cursor.getColumnIndex(BotDbContract.BotsTable.ID_COLUMN)
        val nameColIndex      = cursor.getColumnIndex(BotDbContract.BotsTable.NAME_COLUMN)
        val groupIdColIndex   = cursor.getColumnIndex(BotDbContract.BotsTable.GROUP_ID_COLUMN)
        val groupNameColIndex = cursor.getColumnIndex(BotDbContract.BotsTable.GROUP_NAME_COLUMN)
        val avatarUrlColIndex = cursor.getColumnIndex(BotDbContract.BotsTable.AVATAR_URL_COLUMN)
        val sourceColIndex    = cursor.getColumnIndex(BotDbContract.BotsTable.SOURCE_COLUMN)

        return BotDbContract.BotsTable.BotEntry(
                id        = cursor.getString(idColIndex),
                name      = cursor.getString(nameColIndex),
                groupId   = cursor.getString(groupIdColIndex),
                groupName = cursor.getString(groupNameColIndex),
                avatarUrl = cursor.getString(avatarUrlColIndex),
                source    = cursor.getString(sourceColIndex)
        )
    }

}
