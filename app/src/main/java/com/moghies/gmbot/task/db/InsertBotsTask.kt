package com.moghies.gmbot.task.db

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.os.AsyncTask
import android.support.design.widget.Snackbar
import android.util.Log
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper
import java.sql.SQLException

/**
 * This task asynchronously adds bot entries to the SQLite database
 *
 * Created by mmogh on 6/17/2017.
 */
class InsertBotsTask(val context: Context) : AsyncTask<BotDbContract.BotsTable.BotEntry, Unit, SQLiteException?>() {

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

        // something went wrong...snackbar
        val msg = if (exception != null) {
            if (numBotsToAdd > 1) {
                "One or more bots could not be added"
            } else {
                when (exception) {
                    is SQLiteConstraintException -> "A bot with that ID is already here"
                    else -> "Bot could not be added"
                }
            }
        } else {
            "Bot Added!"
        }

        val view = (context as Activity).window.decorView.findViewById(android.R.id.content)
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
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
