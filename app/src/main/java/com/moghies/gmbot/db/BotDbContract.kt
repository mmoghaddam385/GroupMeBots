package com.moghies.gmbot.db

import android.text.TextUtils
import java.io.Serializable

/**
 * Contract containing all info on the Bot database
 *
 * Created by mmogh on 6/17/2017.
 */
object BotDbContract {

    const val DB_NAME = "bot_database"
    const val DB_VERSION = 1

    object BotsTable {

        const val TABLE_NAME = "bots"

        const val ID_COLUMN = "id" // the id of the bot
        const val NAME_COLUMN = "name" // the name of the bot
        const val GROUP_ID_COLUMN = "group_id" // the id of the group that the bot is in
        const val GROUP_NAME_COLUMN = "group_name" // the name of the group that the bot is in
        const val AVATAR_URL_COLUMN = "avatar_url" // the url of the bot's avatar image
        const val SOURCE_COLUMN = "source" // the name of the bot (i.e. how it was added to the app; manually? through login?)

        const val MANUAL_BOT_SOURCE = "manual"
        const val LOGIN_BOT_SOURCE = "login"

        data class BotEntry(val id:        String,         val source:    String,
                            val name:      String? = null, val groupId:   String? = null,
                            val groupName: String? = null, val avatarUrl: String? = null) : Serializable {

            companion object {
                val GENERIC_BOT = BotEntry(
                        id = "Generic Bot ID",
                        name = "Generic Bot Name",
                        source = MANUAL_BOT_SOURCE
                )
            }

            /**
             * @return the bots name if it has one, its id otherwise
             */
            fun displayName(): String = if (name != null && !TextUtils.isEmpty(name)) name else id

            /**
             * Override the equals function to only check id for equality
             */
            override fun equals(other: Any?): Boolean {
                if (other == null || !(other is BotEntry)) {
                    return false
                }

                val otherBot = other as BotEntry
                return this.id == otherBot.id
            }
        }

    }
}
