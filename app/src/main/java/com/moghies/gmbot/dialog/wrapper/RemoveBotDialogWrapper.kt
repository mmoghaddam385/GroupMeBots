package com.moghies.gmbot.dialog.wrapper

/**
 * This dialog wrapper takes care of removing a bot from the bot list
 *
 * Created by mmogh on 6/18/2017.
 */
class RemoveBotDialogWrapper(val bot: com.moghies.gmbot.db.BotDbContract.BotsTable.BotEntry, val botListAdapter: com.moghies.gmbot.BotListAdapter?, val context: android.content.Context) : DialogWrapper() {

    var onSuccess: ((com.moghies.gmbot.db.BotDbContract.BotsTable.BotEntry) -> Unit)? = null

    init {
        val builder = android.app.AlertDialog.Builder(context)

        when (bot.source) {
            com.moghies.gmbot.db.BotDbContract.BotsTable.MANUAL_BOT_SOURCE -> buildSimpleRemoveDialog(builder)
            com.moghies.gmbot.db.BotDbContract.BotsTable.LOGIN_BOT_SOURCE -> buildSimpleRemoveDialog(builder)
        }

        dialog = builder.create()
    }

    override fun onPositiveButtonClicked(dialog: android.app.AlertDialog) {
        if (bot.source == com.moghies.gmbot.db.BotDbContract.BotsTable.LOGIN_BOT_SOURCE) {
            // TODO: remove from groupme, if successful, continue to remove from local storage
        }

        com.moghies.gmbot.task.db.DeleteBotsTask(context = context, onComplete = { success ->
            if (success) {
                dbDeleteSuccess()
            } else {
                dbDeleteFail()
            }
        }).execute(bot)
    }

    private fun dbDeleteSuccess() {
        botListAdapter?.removeBot(bot)
        val view = (context as android.app.Activity).window.decorView.findViewById(android.R.id.content)
        android.support.design.widget.Snackbar.make(view, "${bot.displayName()} has been removed", android.support.design.widget.Snackbar.LENGTH_SHORT).show()

        onSuccess?.invoke(bot)
    }

    private fun dbDeleteFail() {
        val view = (context as android.app.Activity).window.decorView.findViewById(android.R.id.content)
        android.support.design.widget.Snackbar.make(view, "Unable to remove bot", android.support.design.widget.Snackbar.LENGTH_LONG).show()
    }

    /**
     * The simple remove dialog just removes the bot from the app, not from the users groupme account
     */
    private fun buildSimpleRemoveDialog(builder: android.app.AlertDialog.Builder) {
        builder.setTitle("Remove Bot")
        builder.setMessage("Are you sure you want to remove ${bot.displayName()} from the app? (It will not be removed from GroupMe)")
        builder.setNegativeButton("Cancel", this)
        builder.setPositiveButton("Remove", this)
    }

    /**
     * The ent to end remove dialog asks the user if they want to delete the bot from groupme as well as the app
     */
    private fun buildEndToEndRemoveDialog(builder: android.app.AlertDialog.Builder) {
        TODO("Not Implemented")
    }

}