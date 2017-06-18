package com.moghies.gmbot.task.db

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.util.Log
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.db.BotDbHelper

/**
 * This task selects bots from the SQLite db based on a where clause (if null, all are returned)
 *
 * Created by mmogh on 6/18/2017.
 */
class SelectBotsTask(val context: Context, val whereClause: String? = null, val whereArgs: Array<String>? = null) : AsyncTask<Unit, Unit, List<BotDbContract.BotsTable.BotEntry>>() {

    override fun doInBackground(vararg args: Unit?): List<BotDbContract.BotsTable.BotEntry> {
        val db = BotDbHelper(context).readableDatabase
        val bots = arrayListOf<BotDbContract.BotsTable.BotEntry>()

        val cursor = db.query(BotDbContract.BotsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null)

        while (cursor.moveToNext()) {
            bots += extractBotFromCursor(cursor)
        }

        return bots
    }

    override fun onPostExecute(result: List<BotDbContract.BotsTable.BotEntry>?) {
        super.onPostExecute(result)

        Log.i("select post execute: ", " is succuessss? ${result != null}")
        Log.i("select post execute: ", " found ${result?.size} rows")

        for (bot in result!!) {
            Log.i("select pos execute: ", "\t${bot.toString()}")
        }
    }

    private fun extractBotFromCursor(cursor: Cursor) : BotDbContract.BotsTable.BotEntry {
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