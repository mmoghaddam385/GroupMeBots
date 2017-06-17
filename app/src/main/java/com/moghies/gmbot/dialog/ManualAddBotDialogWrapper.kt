package com.moghies.gmbot.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import com.moghies.gmbot.R
import com.moghies.gmbot.dialog.validator.TextInputRequiredValidator
import java.util.logging.Logger

/**
 * Created by mmogh on 6/15/2017.
 */
class ManualAddBotDialogWrapper(context: Context) : DialogWrapper() {

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
        Log.d("onValidateSuccess", "added bot!")
        dialog.dismiss()
    }

}
