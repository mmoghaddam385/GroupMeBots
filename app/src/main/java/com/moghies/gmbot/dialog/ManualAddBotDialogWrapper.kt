package com.moghies.gmbot.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.LayoutInflater
import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.dialog.validator.TextInputRequiredValidator
import com.moghies.gmbot.task.db.InsertBotsTask

/**
 * Created by mmogh on 6/15/2017.
 */
class ManualAddBotDialogWrapper(val context: Context) : DialogWrapper() {

    private val txtBotId : TextInputEditText
    private val txtBotName : TextInputEditText
    private val txtBotGroup : TextInputEditText

    init {
        val builder = AlertDialog.Builder(context)

        builder.setPositiveButton("Add", null)
        builder.setNegativeButton("Cancel", null)

        val view = LayoutInflater.from(context).inflate(R.layout.layout_add_bot, null)
        txtBotId = view.findViewById(R.id.txtBotId) as TextInputEditText
        txtBotName = view.findViewById(R.id.txtBotName) as TextInputEditText
        txtBotGroup = view.findViewById(R.id.txtBotGroup) as TextInputEditText

        validatorList.add(TextInputRequiredValidator(txtBotId))

        dialog = builder.create()
        dialog!!.setView(view)

        dialog!!.setOnShowListener({dialogInterface ->
            val alertDialog = dialogInterface as AlertDialog

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this)
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(this)
        })

    }

    override fun onValidateSuccess(dialog: AlertDialog) {
        val bot = BotDbContract.BotsTable.BotEntry(
                id = txtBotId.text.toString(),
                name = txtBotName.text.toString(),
                groupName = txtBotGroup.text.toString(),
                source = BotDbContract.BotsTable.MANUAL_BOT_SOURCE
        )

        Log.i(this.javaClass.name, "Adding new bot: $bot")

        InsertBotsTask(context).execute(bot)

        dialog.dismiss()
    }

}
