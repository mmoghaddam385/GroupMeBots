package com.moghies.gmbot.fragment


import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract


/**
 * A simple [Fragment] subclass.
 */
class BotInfoFragment(val bot: BotDbContract.BotsTable.BotEntry) : Fragment() {

    lateinit private var txtBotId: EditText
    lateinit private var txtBotIdWrapper: TextInputLayout

    lateinit private var txtBotName: EditText
    lateinit private var txtBotNameWrapper: TextInputLayout

    lateinit private var txtBotGroup: EditText
    lateinit private var txtBotGroupWrapper: TextInputLayout

    private var isInEditMode = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_bot_info, container, false)

        txtBotId = view.findViewById(R.id.txtBotId) as EditText
        txtBotIdWrapper = view.findViewById(R.id.txtBotIdWrapper) as TextInputLayout

        txtBotName = view.findViewById(R.id.txtBotName) as EditText
        txtBotNameWrapper = view.findViewById(R.id.txtBotNameWrapper) as TextInputLayout

        txtBotGroup = view.findViewById(R.id.txtBotGroup) as EditText
        txtBotGroupWrapper = view.findViewById(R.id.txtBotGroupWrapper) as TextInputLayout

        txtBotId.setText(bot.id, TextView.BufferType.EDITABLE)
        txtBotName.setText(bot.name, TextView.BufferType.EDITABLE)
        txtBotGroup.setText(bot.groupName, TextView.BufferType.EDITABLE)

        view.findViewById(R.id.btnEdit).setOnClickListener { view -> onEditClicked(view as Button) }
        view.findViewById(R.id.btnRemove).setOnClickListener { onRemoveClicked() }

        return view
    }

    private fun onEditClicked(btn: Button) {
        if (isInEditMode) {
            saveChanges()
            btn.setText(R.string.edit_bot)
        } else {
            btn.setText(R.string.save_changes)
            enterEditMode()
        }

        isInEditMode = !isInEditMode
    }

    private fun onRemoveClicked() {

    }

    private fun saveChanges() {
        txtBotName.isEnabled = false
        txtBotGroup.isEnabled = false

        txtBotIdWrapper.clearFocus()
        txtBotNameWrapper.clearFocus()
        txtBotGroupWrapper.clearFocus()

        // TODO: actually save it
    }

    private fun enterEditMode() {
        txtBotName.isEnabled = true
        txtBotGroup.isEnabled = true
    }

}
