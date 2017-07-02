package com.moghies.gmbot.dialog.wrapper

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import com.moghies.gmbot.dialog.validator.Validator
import java.util.*

/**
 * This class describes a wrapper framework for modal dialogs
 * see implementations for details
 *
 * Created by mmogh on 6/15/2017.
 */
abstract class DialogWrapper : DialogInterface.OnClickListener, View.OnClickListener {

    protected var dialog: AlertDialog? = null

    protected val validatorList: MutableList<Validator> = LinkedList<Validator>()

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
     * If it returns true, then [onValidateSuccess] is called
     * otherwise [onValidateFail] is called
     *
     * @return Default implementation loops over validators and will return
     *         false if any validators fail, but runs them all
     */
    protected open fun onDialogValidate(dialog: AlertDialog) : Boolean {
        var isValid = true

        for (validator in validatorList) {
            isValid = isValid && validator.doValidation()
        }

        return isValid
    }

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

        onClick(dialog, which)
    }
}