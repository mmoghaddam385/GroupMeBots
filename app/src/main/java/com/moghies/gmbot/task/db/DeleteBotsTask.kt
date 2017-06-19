package com.moghies.gmbot.task.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper

/**
 * This task deletes bots from the SQLite db
 * based on a given where clause if given, otherwise based on bot entries passed into execute
 *
 * @constructor
 * @param[onComplete] lambda to be executed on complete; the Boolean param denotes whether or not the delete was successful
 *
 * Created by mmogh on 6/18/2017.
 */
class DeleteBotsTask(val context: Context, var whereClause: String? = null,
                     var whereArgs: Array<String>? = null, val onComplete: ((Boolean) -> Unit)? = null): AsyncTask<BotDbContract.BotsTable.BotEntry, Unit, Boolean>() {

    override fun doInBackground(vararg bots: BotDbContract.BotsTable.BotEntry?): Boolean {
        var success = true

        BotDbHelper(context).writableDatabase.use { db ->

            // delete based on where clause
            if (whereClause != null) {
                success = doDelete(db)
            } else {
                constructWhereClause(bots.asList())
                success = doDelete(db)
            }
        }

        return success
    }

    override fun onPostExecute(exception: Boolean?) {
        super.onPostExecute(exception)
        onComplete?.invoke(exception ?: false)
    }

    private fun constructWhereClause(bots : List<BotDbContract.BotsTable.BotEntry?>) {
        val builder = StringBuilder()
        val args = ArrayList<String>()

        for (bot in bots) {
            if (bot != null) {
                builder.append("${BotDbContract.BotsTable.ID_COLUMN} = ? OR ")
                args.add(bot.id)
            }
        }

        // remove the last OR expr
        builder.delete(builder.length - 4, builder.length)
        builder.append(";")
        whereClause = builder.toString()

        whereArgs = args.toArray(emptyArray<String>())
    }

    private fun doDelete(db: SQLiteDatabase) : Boolean {
        val result = db.delete(BotDbContract.BotsTable.TABLE_NAME, whereClause, whereArgs)
        return result != -1
    }

}