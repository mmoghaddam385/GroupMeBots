package com.moghies.gmbot.task.db

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper

/**
 * This task asynchronously adds bot entries to the SQLite database
 *
 * Created by mmogh on 6/17/2017.
 */
class InsertBotsTask(val context: Context) : AsyncTask<BotDbContract.BotsTable.BotEntry, Unit, Boolean>() {

    override fun doInBackground(vararg bots : BotDbContract.BotsTable.BotEntry?):  Boolean {
        var success = true

        BotDbHelper(context).writableDatabase.use { db ->

            // attempt to insert each bot
            for (bot in bots) {
                if (bot != null) {
                    val result = db.insert(BotDbContract.BotsTable.TABLE_NAME, null, getContentValues(bot))

                    // if result is -1, there was a problem :(
                    if (result == -1L) {
                        success = false
                    }
                }
            }
        }

        return success
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        Log.i("onpost exexte", "succes? ${result}")
    }

    private fun getContentValues(bot: BotDbContract.BotsTable.BotEntry) : ContentValues {
        val values = ContentValues()

        values.put(BotDbContract.BotsTable.ID_COLUMN, bot.id)
        values.put(BotDbContract.BotsTable.SOURCE_COLUMN, bot.source)

        if (bot.name      != null) values.put(BotDbContract.BotsTable.NAME_COLUMN,       bot.name)
        if (bot.groupId   != null) values.put(BotDbContract.BotsTable.GROUP_ID_COLUMN,   bot.groupId)
        if (bot.groupName != null) values.put(BotDbContract.BotsTable.GROUP_NAME_COLUMN, bot.groupName)
        if (bot.avatarUrl != null) values.put(BotDbContract.BotsTable.AVATAR_URL_COLUMN, bot.avatarUrl)

        return values
    }

}
