package com.moghies.gmbot.task.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper

/**
 * Async task to update an existing bot
 *
 * @param[bot] the bot to update; ID is used in where clause to identify which row to update
 *
 * Created by mmogh on 6/25/2017.
 */
class UpdateBotTask(val bot: BotDbContract.BotsTable.BotEntry, val context: Context) : AsyncTask<Unit, Unit, Unit>() {

    override fun doInBackground(vararg p0: Unit?) {
        BotDbHelper.Factory.get(context).writableDatabase.use { db ->
            db.update(BotDbContract.BotsTable.TABLE_NAME, getContentValues(bot), "${BotDbContract.BotsTable.ID_COLUMN} = ?", arrayOf(bot.id))
        }
    }

    private fun getContentValues(bot: BotDbContract.BotsTable.BotEntry) : ContentValues {
        val values = ContentValues()

        values.put(BotDbContract.BotsTable.SOURCE_COLUMN, bot.source)

        bot.name?.let      { values.put(BotDbContract.BotsTable.NAME_COLUMN,       it) }
        bot.groupId?.let   { values.put(BotDbContract.BotsTable.GROUP_ID_COLUMN,   it) }
        bot.groupName?.let { values.put(BotDbContract.BotsTable.GROUP_NAME_COLUMN, it) }
        bot.avatarUrl?.let { values.put(BotDbContract.BotsTable.AVATAR_URL_COLUMN, it) }

        return values
    }
}