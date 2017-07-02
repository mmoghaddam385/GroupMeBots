package com.moghies.gmbot.dialog.wrapper

import com.moghies.gmbot.db.BotDbContract

/**
 * This dialog wrapper takes care of manually adding a bot
 *
 * Created by mmogh on 6/15/2017.
 */
class ManualAddBotDialogWrapper(val context: android.content.Context, val botListAdapter: com.moghies.gmbot.BotListAdapter) : DialogWrapper() {

    private val txtBotId : android.support.design.widget.TextInputEditText
    private val txtBotName : android.support.design.widget.TextInputEditText
    private val txtBotGroup : android.support.design.widget.TextInputEditText

    init {
        val builder = android.app.AlertDialog.Builder(context)

        builder.setPositiveButton("Add", null)
        builder.setNegativeButton("Cancel", null)

        val view = android.view.LayoutInflater.from(context).inflate(com.moghies.gmbot.R.layout.layout_add_bot, null)
        txtBotId = view.findViewById(com.moghies.gmbot.R.id.txtBotId) as android.support.design.widget.TextInputEditText
        txtBotName = view.findViewById(com.moghies.gmbot.R.id.txtBotName) as android.support.design.widget.TextInputEditText
        txtBotGroup = view.findViewById(com.moghies.gmbot.R.id.txtBotGroup) as android.support.design.widget.TextInputEditText

        validatorList.add(com.moghies.gmbot.dialog.validator.TextInputRequiredValidator(txtBotId))

        dialog = builder.create()
        dialog!!.setView(view)

        dialog!!.setOnShowListener({dialogInterface ->
            val alertDialog = dialogInterface as android.app.AlertDialog

            alertDialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE).setOnClickListener(this)
            alertDialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE).setOnClickListener(this)
        })

    }

    override fun onValidateSuccess(dialog: android.app.AlertDialog) {
        val bot = com.moghies.gmbot.db.BotDbContract.BotsTable.BotEntry(
                id = txtBotId.text.toString(),
                name = txtBotName.text.toString(),
                groupName = txtBotGroup.text.toString(),
                source = BotDbContract.BotsTable.MANUAL_BOT_SOURCE
        )

        android.util.Log.i(this.javaClass.name, "Adding new bot: $bot")

        com.moghies.gmbot.task.db.InsertBotsTask(context, { exception ->
            if (exception != null) {
                onAddFail(exception)
            } else {
                onAddSuccess(bot)
            }
        }).execute(bot)

        dialog.dismiss()
    }

    override fun onNegativeButtonClicked(dialog: android.app.AlertDialog) {
        dialog.dismiss()
    }

    private fun onAddSuccess(bot: com.moghies.gmbot.db.BotDbContract.BotsTable.BotEntry) {
        botListAdapter.addBot(bot)

        val view = (context as android.app.Activity).window.decorView.findViewById(android.R.id.content)
        android.support.design.widget.Snackbar.make(view, "Bot added!", android.support.design.widget.Snackbar.LENGTH_LONG).show()
    }

    private fun onAddFail(exception: android.database.sqlite.SQLiteException) {
        val msg = if (exception is android.database.sqlite.SQLiteConstraintException) {
            "A bot with that ID is already here!"
        } else {
            "Bot could not be added"
        }

        val view = (context as android.app.Activity).window.decorView.findViewById(android.R.id.content)
        android.support.design.widget.Snackbar.make(view, msg, android.support.design.widget.Snackbar.LENGTH_LONG).show()
    }

}
