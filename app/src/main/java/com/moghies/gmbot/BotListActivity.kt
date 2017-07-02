package com.moghies.gmbot

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.dialog.ReplaceBotsDialog
import com.moghies.gmbot.dialog.wrapper.ManualAddBotDialogWrapper
import com.moghies.gmbot.task.db.InsertBotsTask
import com.moghies.gmbot.task.db.UpdateBotTask

class BotListActivity : AppCompatActivity() {

    var lvBotList: ListView? = null
    val lvBotListAdapter = BotListAdapter(this::onBotClicked)

    lateinit private var fabMenu: FloatingActionsMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot_list)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Bot List"
        setSupportActionBar(toolbar)

        fabMenu = findViewById(R.id.fabAddMenu) as FloatingActionsMenu

        (findViewById(R.id.fabManualAdd) as FloatingActionButton).setOnClickListener { showAddBotDialog(); fabMenu.collapse() }
        (findViewById(R.id.fabLoginAdd) as FloatingActionButton).setOnClickListener { startLoginActivity(); fabMenu.collapse() }

        lvBotList = findViewById(R.id.lvBotList) as ListView

        lvBotList!!.adapter = lvBotListAdapter
        lvBotListAdapter.loadFromStorage(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val bot = data?.getSerializableExtra(BotViewActivity.BOT_ENTRY_BUNDLE_ID) as BotDbContract.BotsTable.BotEntry?
        val bots = data?.getSerializableExtra(GroupMeAuthActivity.BOTS_RECEIVED_EXTRA) as Array<BotDbContract.BotsTable.BotEntry>?

        when (resultCode) {
            BotViewActivity.BOT_DELETED_RESULT -> bot?.let { lvBotListAdapter.removeBot(it) }
            BotViewActivity.BOT_MODIFIED_RESULT -> bot?.let { lvBotListAdapter.updateBot(it) }
            GroupMeAuthActivity.BOTS_RECEIVED_RESULT -> addNewBots(bots)
            GroupMeAuthActivity.ERROR_RECEIVING_BOTS_RESULT -> showSnackbar(R.string.cannot_load_bots)
        }
    }

    /**
     * Add multiple new bots to the list
     */
    private fun addNewBots(bots: Array<BotDbContract.BotsTable.BotEntry>?) {

        if (bots != null) {
            val groups = bots.groupBy { it in lvBotListAdapter }
            val botsToAdd = groups[false] // the bots that aren't in the bot list already
            val existingBots = groups[true] // the bots that are already in the bot list

            if (existingBots != null && existingBots.isNotEmpty()){
                ReplaceBotsDialog(this, existingBots, { toReplace ->
                    for (bot in toReplace) {
                        UpdateBotTask(bot, this).execute()
                        lvBotListAdapter.updateBot(bot)
                    }
                }).show(fragmentManager, "some fragment")
            }

            if (botsToAdd != null && botsToAdd.isNotEmpty()) {

                InsertBotsTask(this, { exception ->
                    if (exception == null) {
                        lvBotListAdapter.addAll(botsToAdd)
                    } else {
                        showSnackbar(R.string.cannot_load_bots)
                    }
                }).execute(*botsToAdd.toTypedArray())
            }

        } else {
            showSnackbar(R.string.cannot_load_bots)
        }
    }

    private fun showSnackbar(@StringRes msgRes: Int, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(findViewById(android.R.id.content), msgRes, length).show()
    }

    /**
     * Creates and shows the add bot dialog modal
     */
    private fun showAddBotDialog() {
        ManualAddBotDialogWrapper(this, lvBotListAdapter).show()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, GroupMeAuthActivity::class.java)
        startActivityForResult(intent, 0)
    }

    private fun onBotClicked(bot: BotDbContract.BotsTable.BotEntry) {
        val intent = Intent(this, BotViewActivity::class.java)
        intent.putExtra(BotViewActivity.BOT_ENTRY_BUNDLE_ID, bot)

        startActivityForResult(intent, 0)
    }

}
