package com.moghies.gmbot.fragment


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.moghies.gmbot.BotViewActivity

import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.dialog.RemoveBotDialogWrapper
import com.moghies.gmbot.task.db.UpdateBotTask


/**
 * A simple [Fragment] subclass.
 */
class BotInfoFragment(var bot: BotDbContract.BotsTable.BotEntry) : Fragment() {

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
        val dialog = RemoveBotDialogWrapper(bot, null, this.context)
        dialog.onSuccess = {
            val result = Intent()
            result.putExtra(BotViewActivity.BOT_ENTRY_BUNDLE_ID, bot)

            this.activity.setResult(BotViewActivity.BOT_DELETED_RESULT, result)
            this.activity.finish()
        }

        dialog.show()
    }

    private fun saveChanges() {
        txtBotName.isEnabled = false
        txtBotGroup.isEnabled = false

        txtBotIdWrapper.clearFocus()
        txtBotNameWrapper.clearFocus()
        txtBotGroupWrapper.clearFocus()

        bot = bot.copy(
                name = txtBotName.text.toString(),
                groupName = txtBotGroup.text.toString()
        )

        // update the title bar if it exists
        (this.activity as? AppCompatActivity)?.supportActionBar?.title = bot.displayName()

        UpdateBotTask(bot, context).execute()

        val intent = Intent()
        intent.putExtra(BotViewActivity.BOT_ENTRY_BUNDLE_ID, bot)
        this.activity.setResult(BotViewActivity.BOT_MODIFIED_RESULT, intent)
    }

    private fun enterEditMode() {
        txtBotName.isEnabled = true
        txtBotGroup.isEnabled = true
    }

}
