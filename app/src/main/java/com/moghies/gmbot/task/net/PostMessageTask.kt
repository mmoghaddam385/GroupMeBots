package com.moghies.gmbot.task.net

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.support.design.widget.Snackbar
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * This task posts a message to groupme as a bot. It shows a loading dialog on preExecute and a status SnackBar onPostExecute
 *
 * Created by mmogh on 6/25/2017.
 */
class PostMessageTask(val botId: String, val message: String, val context: Context, val onComplete: (Int) -> Unit) : AsyncTask<Unit, Unit, Int>() {

    private companion object {
        const val POST_MESSAGE_URL = "https://api.groupme.com/v3/bots/post"
        const val BOT_ID_PARAM = "bot_id"
        const val MESSAGE_TEXT_PARAM = "text"
    }

    lateinit private var progessDialog: ProgressDialog

    override fun onPreExecute() {
        progessDialog = ProgressDialog.show(context, null, "Sending Message...")
    }

    override fun doInBackground(vararg p0: Unit?) : Int {
        val url = URL(POST_MESSAGE_URL)
        var conn : HttpURLConnection? = null
        var statusCode = 500

        try {
            conn = url.openConnection() as HttpURLConnection

            conn.requestMethod = "POST"

            conn.doOutput = true
            conn.getOutputStream().write(getBody())

            conn.connect()
            statusCode = conn.responseCode
        } finally {
            conn?.disconnect()
        }

        return statusCode
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)

        progessDialog.hide()

        val msg = when (result) {
            202 -> "Message sent"
            404 -> "This bot doesn't exist in GroupMe!"
            else -> "Something went wrong, the message wasn't sent"
        }

        val view = (context as Activity).window.decorView.findViewById(android.R.id.content)
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()

        onComplete(result ?: 500)
    }

    private fun getBody() : ByteArray {
        val json = JSONObject()

        json.put(BOT_ID_PARAM, botId)
        json.put(MESSAGE_TEXT_PARAM, message)

        return json.toString().toByteArray()
    }

}