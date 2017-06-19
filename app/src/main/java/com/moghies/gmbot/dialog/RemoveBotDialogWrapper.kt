package com.moghies.gmbot.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.design.widget.Snackbar
import com.moghies.gmbot.BotListAdapter
import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.task.db.DeleteBotsTask

/**
 * This dialog wrapper takes care of removing a bot from the bot list
 *
 * Created by mmogh on 6/18/2017.
 */
class RemoveBotDialogWrapper(val bot: BotDbContract.BotsTable.BotEntry, val botListAdapter: BotListAdapter, val context: Context) : DialogWrapper() {

    init {
        val builder = AlertDialog.Builder(context)

        when (bot.source) {
            BotDbContract.BotsTable.MANUAL_BOT_SOURCE -> buildSimpleRemoveDialog(builder)
            BotDbContract.BotsTable.LOGIN_BOT_SOURCE -> buildEndToEndRemoveDialog(builder)
        }

        dialog = builder.create()
    }

    override fun onPositiveButtonClicked(dialog: AlertDialog) {
        if (bot.source == BotDbContract.BotsTable.LOGIN_BOT_SOURCE) {
            // TODO: remove from groupme, if successful, continue to remove from local storage
        }

        DeleteBotsTask(context = context, onComplete = {success ->
            if (success) {
                dbDeleteSuccess()
            } else {
                dbDeleteFail()
            }
        }).execute(bot)
    }

    private fun dbDeleteSuccess() {
        botListAdapter.removeBot(bot)
        val view = (context as Activity).window.decorView.findViewById(android.R.id.content)
        Snackbar.make(view, "${bot.displayName()} has been removed", Snackbar.LENGTH_SHORT).show()
    }

    private fun dbDeleteFail() {
        val view = (context as Activity).window.decorView.findViewById(android.R.id.content)
        Snackbar.make(view, "Unable to remove bot", Snackbar.LENGTH_LONG).show()
    }

    /**
     * The simple remove dialog just removes the bot from the app, not from the users groupme account
     */
    private fun buildSimpleRemoveDialog(builder: AlertDialog.Builder) {
        builder.setTitle("Remove Bot")
        builder.setMessage("Are you sure you want to remove ${bot.displayName()} from the app? (It will not be removed from GroupMe)")
        builder.setNegativeButton("Cancel", this)
        builder.setPositiveButton("Remove", this)
    }

    /**
     * The ent to end remove dialog asks the user if they want to delete the bot from groupme as well as the app
     */
    private fun buildEndToEndRemoveDialog(builder: AlertDialog.Builder) {
        TODO("Not Implemented")
    }

}