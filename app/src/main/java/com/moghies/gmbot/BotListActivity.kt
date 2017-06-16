package com.moghies.gmbot

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.moghies.gmbot.dialog.ManualAddBotDialogWrapper

class BotListActivity : AppCompatActivity(), DialogInterface.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot_list)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Bot List"
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { _ -> showAddBotDialog()}
    }

    /**
     * Creates and shows the add bot dialog modal
     */
    private fun showAddBotDialog() {
        ManualAddBotDialogWrapper(this).show()
    }

    private fun validateAddBotDialog(dialog: Dialog) {
        val botId = (dialog.findViewById(R.id.txtBotId) as EditText).text
        val botName = (dialog.findViewById(R.id.txtBotName) as EditText).text
        val botGroup = (dialog.findViewById(R.id.txtBotGroup) as EditText).text

        // only botId is required
        if (TextUtils.isEmpty(botId)) {

        }
    }


    /**
     * on dialog click
     */
    override fun onClick(dialog: DialogInterface?, btn: Int) {
        if (btn == DialogInterface.BUTTON_POSITIVE) {
            validateAddBotDialog(dialog as AlertDialog)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_bot_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
