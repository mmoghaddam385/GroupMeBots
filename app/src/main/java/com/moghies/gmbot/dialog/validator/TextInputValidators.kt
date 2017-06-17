package com.moghies.gmbot.dialog.validator

import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log

/**
 * This base class sets up the framework for specific validators to build off of
 */
abstract class TextInputValidator(val target: TextInputEditText, var fieldName: String = "") : Validator(), TextWatcher {

    val parent: TextInputLayout

    init {
        target.addTextChangedListener(this)
        parent = target.parent.parent as TextInputLayout

        if (TextUtils.isEmpty(fieldName)) {
            fieldName = parent.hint.toString()
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}

/**
 * This validator checks if a TextInputEditText is empty
 *
 * Created by mmogh on 6/17/2017.
 */
class TextInputRequiredValidator(target: TextInputEditText, fieldName: String = "") : TextInputValidator(target, fieldName) {

    override fun doValidation(): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(target.text)) {
            parent.error = "$fieldName is required"
            isValid = false
        } else {
            parent.error = null
        }

        parent.isErrorEnabled = !isValid
        return isValid
    }

    override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        doValidation()

        // don't show the error message if the user just backspaced too much
        parent.isErrorEnabled = false
    }

}