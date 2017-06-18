package com.moghies.gmbot.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Helper class to access the SQLite database
 *
 * Created by mmogh on 6/17/2017.
 */
class BotDbHelper(context: Context) : SQLiteOpenHelper(context, BotDbContract.DB_NAME,
                                                       null, BotDbContract.DB_VERSION) {

    /**
     * this method is called the first time a db is requested in the app
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db!! // assert that the db isnt null

        createBotsTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // there's only one version of the db so far
        TODO("not implemented")
    }

    private fun createBotsTable(db: SQLiteDatabase) {
        val builder = StringBuilder()

        builder.append("CREATE TABLE ${BotDbContract.BotsTable.TABLE_NAME} (")
        builder.append("${BotDbContract.BotsTable.ID_COLUMN}         TEXT   PRIMARY KEY  NOT NULL, ")
        builder.append("${BotDbContract.BotsTable.NAME_COLUMN}       TEXT, ")
        builder.append("${BotDbContract.BotsTable.GROUP_ID_COLUMN}   TEXT, ")
        builder.append("${BotDbContract.BotsTable.GROUP_NAME_COLUMN} TEXT, ")
        builder.append("${BotDbContract.BotsTable.AVATAR_URL_COLUMN} TEXT, ")
        builder.append("${BotDbContract.BotsTable.SOURCE_COLUMN}     TEXT   NOT NULL ")
        builder.append("); ")

        db.execSQL(builder.toString())
    }
}