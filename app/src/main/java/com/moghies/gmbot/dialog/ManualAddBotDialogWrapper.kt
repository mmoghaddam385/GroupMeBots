package com.moghies.gmbot.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import com.moghies.gmbot.R
import java.util.logging.Logger

/**
 * Created by mmogh on 6/15/2017.
 */
class ManualAddBotDialogWrapper(context: Context) : DialogWrapper() {

    private var botId: String = ""
    private var botName: String = ""
    private var botGroup: String = ""

    init {
        val builder = AlertDialog.Builder(context)

        builder.setPositiveButton("Add", null)
        builder.setNegativeButton("Cancel", null)



        dialog = builder.create()
        dialog!!.setView(LayoutInflater.from(context).inflate(R.layout.layout_add_bot, null))

        dialog!!.setOnShowListener({dialogInterface ->
            val alertDialog = dialogInterface as AlertDialog

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this)
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(this)
        })

    }

    override fun onDialogValidate(dialog: AlertDialog): Boolean {
        extractInputs(dialog)

        // only required field is botId
        return !TextUtils.isEmpty(botId)
    }

    override fun onValidateSuccess(dialog: AlertDialog) {
        Log.i("Manual add bot dialog", "Adding bot!!")
        dialog.dismiss()
    }

    override fun onValidateFail(dialog: AlertDialog) {
        (dialog.findViewById(R.id.txtBotIdWrapper) as TextInputLayout).error = "Bot id is required"
    }

    private fun extractInputs(dialog: AlertDialog) {
        botId = (dialog.findViewById(R.id.txtBotId) as EditText).text.toString()
        botName = (dialog.findViewById(R.id.txtBotName) as EditText).text.toString()
        botGroup = (dialog.findViewById(R.id.txtBotGroup) as EditText).text.toString()
    }

}