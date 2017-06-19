package com.moghies.gmbot.task.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.os.AsyncTask
import android.util.Log
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper

/**
 * This task asynchronously adds bot entries to the SQLite database
 *
 * @constructor
 * @param[onComplete] lambda to be executed upon task completion with the parameter being whatever exception (if any) was encountered while inserting
 *
 * Created by mmogh on 6/17/2017.
 */
class InsertBotsTask(val context: Context, val onComplete: ((SQLiteException?) -> Unit)? = null) : AsyncTask<BotDbContract.BotsTable.BotEntry, Unit, SQLiteException?>() {

    var numBotsToAdd: Int = 0

    override fun doInBackground(vararg bots : BotDbContract.BotsTable.BotEntry?):  SQLiteException? {
        var exception: SQLiteException? = null
        numBotsToAdd = bots.size

        BotDbHelper(context).writableDatabase.use { db ->

            try {
                // attempt to insert each bot
                for (bot in bots) {
                    if (bot != null) {
                        db.insertOrThrow(BotDbContract.BotsTable.TABLE_NAME, null, getContentValues(bot))
                    }
                }
            } catch (ex: SQLiteException) {
                Log.e(this.javaClass.name, "Error inserting bot: ", ex)
                exception = ex
            }
        }

        return exception
    }

    override fun onPostExecute(exception: SQLiteException?) {
        super.onPostExecute(exception)
        Log.i(this.javaClass.name, "Post Insert; succes? ${exception == null}")

        onComplete?.invoke(exception)
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
