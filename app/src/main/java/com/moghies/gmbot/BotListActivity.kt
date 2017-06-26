package com.moghies.gmbot

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.dialog.ManualAddBotDialogWrapper

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

        lvBotList = findViewById(R.id.lvBotList) as ListView

        lvBotList!!.adapter = lvBotListAdapter
        lvBotListAdapter.loadFromStorage(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val bot = data?.getSerializableExtra(BotViewActivity.BOT_ENTRY_BUNDLE_ID) as BotDbContract.BotsTable.BotEntry?

        when (resultCode) {
            BotViewActivity.BOT_DELETED_RESULT -> bot?.let { lvBotListAdapter.removeBot(it) }
            BotViewActivity.BOT_MODIFIED_RESULT -> bot?.let { lvBotListAdapter.updateBot(it) }
        }
    }

    /**
     * Creates and shows the add bot dialog modal
     */
    private fun showAddBotDialog() {
        ManualAddBotDialogWrapper(this, lvBotListAdapter).show()
    }

    private fun onBotClicked(bot: BotDbContract.BotsTable.BotEntry) {
        val intent = Intent(this, BotViewActivity::class.java)
        intent.putExtra(BotViewActivity.BOT_ENTRY_BUNDLE_ID, bot)

        startActivityForResult(intent, 0)
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
