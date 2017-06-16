package com.moghies.gmbot.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.View

/**
 * Created by mmogh on 6/15/2017.
 */
abstract class DialogWrapper : DialogInterface.OnClickListener, View.OnClickListener {

    protected var dialog: AlertDialog? = null

    fun show() {
        dialog!!.show()
    }

    /**
     * Called when the positive button is clicked, but before input is validated
     */
    protected open fun onPositiveButtonClicked(dialog: AlertDialog) {}

    protected open fun onNegativeButtonClicked(dialog: AlertDialog) {}

    protected open fun onNeutralButtonClicked(dialog: AlertDialog) {}

    /**
     * Called when the positive button is clicked, after onPositiveButtonClicked(dialog)
     * This function will call onValidateSuccess if it returns true
     * and onValidateFail if it returns false
     */
    protected open fun onDialogValidate(dialog: AlertDialog) : Boolean = true
    protected open fun onValidateSuccess(dialog: AlertDialog) {}
    protected open fun onValidateFail(dialog: AlertDialog) {}

    /**
     * Redirects dialog click events to the proper handlers
     */
    override fun onClick(dialog: DialogInterface?, btn: Int) {
        val alertDialog = dialog as AlertDialog
        when (btn) {
            DialogInterface.BUTTON_POSITIVE -> {
                onPositiveButtonClicked(alertDialog)

                if (onDialogValidate(alertDialog)) {
                    onValidateSuccess(alertDialog)
                } else {
                    onValidateFail(alertDialog)
                }
            }
            DialogInterface.BUTTON_NEUTRAL -> onNeutralButtonClicked(alertDialog)
            DialogInterface.BUTTON_NEGATIVE -> onNegativeButtonClicked(alertDialog)
        }
    }

    /**
     * redirects raw click events from buttons
     */
    override fun onClick(view: View?) {
        val which = when (view!!.id) {
            android.R.id.button1 -> DialogInterface.BUTTON_POSITIVE
            android.R.id.button2 -> DialogInterface.BUTTON_NEGATIVE
            android.R.id.button3 -> DialogInterface.BUTTON_NEUTRAL
            else -> -1
        }

        Log.i("tag", "This is the id: ${view!!.id}")
        Log.i("tag", "This is which: $which")

        onClick(dialog, which)
    }
}