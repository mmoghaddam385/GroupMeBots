package com.moghies.gmbot.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.annotation.VisibleForTesting

/**
 * Helper class to access the SQLite database
 *
 * Use [Factory.get] inner object to get an instance
 *
 * Created by mmogh on 6/17/2017.
 */
class BotDbHelper private constructor(context: Context, dbName: String) : SQLiteOpenHelper(context, dbName, null, BotDbContract.DB_VERSION) {

    object Factory {

        /**
         * the name of the db to use, change only for testing purposes
         */
        var DB_NAME = BotDbContract.DB_NAME

        /**
         * Get an instance of [BotDbHelper]
         */
        fun get(context: Context): BotDbHelper {
            return BotDbHelper(context, DB_NAME)
        }
    }

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