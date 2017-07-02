package com.moghies.gmbot.task.net

import android.os.AsyncTask
import android.util.Log
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.moghies.gmbot.db.BotDbContract
import org.json.JSONArray
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * This task queries all bots created by a user (identified by their access token)
 *
 * Created by mmogh on 7/1/2017.
 */
class BotIndexTask(val token: String, val onComplete: (List<BotDbContract.BotsTable.BotEntry>?) -> Unit) : AsyncTask<Unit, Unit, Boolean>() {

    private companion object {
        const val BOT_INDEX_URL = "https://api.groupme.com/v3/bots"
        const val GROUP_INFO_URL = "https://api.groupme.com/v3/groups/"

        const val RESPONSE_FIELD = "response"
        const val BOT_ID_FIELD = "bot_id"
        const val GROUP_ID_FIELD = "group_id"
        const val NAME_FIELD = "name"
        const val AVATAR_URL_FIELD = "avatar_url"
    }

    private val bots: MutableList<BotDbContract.BotsTable.BotEntry> = mutableListOf()

    override fun doInBackground(vararg p0: Unit?): Boolean {
        val url = URL(BOT_INDEX_URL + "?token=$token")
        var conn: HttpURLConnection? = null

        try {
            conn = url.openConnection() as HttpURLConnection

            conn.requestMethod = "GET"
            conn.doInput = true

            conn.connect()

            if (conn.responseCode == 200) {
                return parseIndexResponse(conn.inputStream)

            } else {
                Log.e("BotIndexTask", "Not 200 response from bot index: ${conn.responseCode}; ${conn.responseMessage}")
                return false
            }

        } finally {
            conn?.disconnect()
        }
    }

    /**
     * parse the response of the bot index api call
     *
     * @return true if successful, false otherwise
     */
    private fun parseIndexResponse(stream: InputStream): Boolean {
        val mapper = ObjectMapper()
        val botArr = mapper.readTree(stream)[RESPONSE_FIELD] as? ArrayNode

        if (botArr != null) {
            for (node in botArr) {
                bots.add(parseBot(node as ObjectNode))
            }

            return true
        } else {
            return false
        }
    }

    /**
     * Parse a JSON ObjectNode as a bot
     */
    private fun parseBot(botNode: ObjectNode): BotDbContract.BotsTable.BotEntry {
        val groupName = getGroupName(botNode[GROUP_ID_FIELD].textValue())

        val bot = BotDbContract.BotsTable.BotEntry(
                id = botNode[BOT_ID_FIELD].textValue(),
                name = botNode[NAME_FIELD].textValue(),
                groupId = botNode[GROUP_ID_FIELD].textValue(),
                groupName = groupName,
                avatarUrl = botNode[AVATAR_URL_FIELD].textValue(),
                source = BotDbContract.BotsTable.LOGIN_BOT_SOURCE
        )

        Log.i("parseBot", "Heres a bot: $bot")

        return bot
    }

    /**
     * Query GroupMe for the name of the group associated with the given ID
     *
     * @return the name of the group, or empty string if it cannot be determined
     */
    private fun getGroupName(groupId: String): String {
        val url = URL(GROUP_INFO_URL + groupId + "?token=$token")
        var conn: HttpURLConnection? = null

        try {
            conn = url.openConnection() as HttpURLConnection

            conn.requestMethod = "GET"
            conn.doInput = true

            conn.connect()

            if (conn.responseCode == 200) {
                return parseGroupName(conn.inputStream)

            } else {
                Log.e("BotIndexTask", "Not 200 response from group info (group ID = $groupId): ${conn.responseCode}; ${conn.responseMessage}")
                return ""
            }
        } finally {
            conn?.disconnect()
        }
    }

    /**
     * Extract the name from a group info response stream
     */
    private fun parseGroupName(stream: InputStream): String {
        return ObjectMapper().readTree(stream)[RESPONSE_FIELD][NAME_FIELD].textValue()
    }

    override fun onPostExecute(success: Boolean?) {
        onComplete(if (success ?: false) bots else null)
    }
}