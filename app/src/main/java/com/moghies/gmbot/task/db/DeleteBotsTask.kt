package com.moghies.gmbot.task.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.util.Log
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper

/**
 * This task deletes bots from the SQLite db
 * based on a given where clause if given, otherwise based on bot entries passed into execute
 *
 * Created by mmogh on 6/18/2017.
 */
class DeleteBotsTask(val context: Context, var whereClause: String? = null, var whereArgs: Array<String>? = null): AsyncTask<BotDbContract.BotsTable.BotEntry, Unit, Boolean>() {

    override fun doInBackground(vararg bots: BotDbContract.BotsTable.BotEntry?): Boolean {
        val db = BotDbHelper(context).writableDatabase
        var success = true

        // delete based on where clause
        if (whereClause != null) {
            success = doDelete(db)
        } else {
            constructWhereClause(bots.asList())
            success = doDelete(db)
        }

        return success
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        Log.i("on post exexute", "was success!?!?! ${result}")
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
        builder.removeSuffix(" OR ")
        whereClause = builder.toString()

        whereArgs = args.toArray(emptyArray<String>())
    }

    private fun doDelete(db: SQLiteDatabase) : Boolean {
        val result = db.delete(BotDbContract.BotsTable.TABLE_NAME, whereClause, whereArgs)
        return result != -1
    }

}