package com.moghies.gmbot.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.task.net.PostMessageTask


/**
 * A simple [Fragment] subclass.
 * Use the [BotMessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BotMessageFragment(val bot: BotDbContract.BotsTable.BotEntry) : Fragment(), View.OnClickListener {

    private lateinit var txtMessage: EditText
    private lateinit var chkAddAttachments: CheckBox
    private lateinit var attachmentView: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_bot_message, container, false)

        txtMessage = view.findViewById(R.id.txtMessage) as EditText
        chkAddAttachments = view.findViewById(R.id.chkAddAttachment) as CheckBox
        attachmentView = view.findViewById(R.id.txtAttachmentPlaceholder)

        (view.findViewById(R.id.btnClear) as Button).setOnClickListener(this)
        (view.findViewById(R.id.btnSend)  as Button).setOnClickListener(this)

        chkAddAttachments.setOnCheckedChangeListener(this::onAddAttachmentsCheckChanged)

        return view
    }

    override fun onClick(btn: View?) {
        if (btn != null) {
            when (btn.id) {
                R.id.btnClear -> clearFields()
                R.id.btnSend -> sendMessage()
            }
        }
    }

    private fun onAddAttachmentsCheckChanged(view: CompoundButton, checked: Boolean) {
        attachmentView.visibility = if (checked) View.VISIBLE else View.GONE
    }

    private fun clearFields() {
        txtMessage.setText("", TextView.BufferType.EDITABLE)
        chkAddAttachments.isChecked = false
    }

    private fun sendMessage() {
        if (validateFields()) {
            PostMessageTask(bot.id, txtMessage.text.toString(), this.activity, {
                clearFields()
            }).execute()
        }
    }

    /**
     * text cannot be empty if there is no attachment
     */
    private fun validateFields() : Boolean {
        if (!chkAddAttachments.isChecked && TextUtils.isEmpty(txtMessage.text.toString())) {
            showValidationDialog("Message Text cannot be empty if there is no attachment!")
            return false
        }

        return true
    }

    private fun showValidationDialog(msg: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(msg)
        builder.setPositiveButton("Ok", null)

        builder.create().show()
    }

}
