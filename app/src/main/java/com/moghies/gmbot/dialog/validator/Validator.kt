package com.moghies.gmbot.dialog.validator

/**
 * This class describes the contract of a validator
 * Validators can be attached to all sorts of components
 *
 * Created by mmogh on 6/17/2017.
 */
abstract class Validator {

    /**
     * @return true if valid, false otherwise
     */
    abstract fun doValidation() : Boolean
}