package com.moghies.gmbot.view

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.moghies.gmbot.BotListAdapter
import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.dialog.wrapper.RemoveBotDialogWrapper
import java.net.URL

/**
 * Class that represents a bot list row item
 *
 * Created by mmogh on 6/18/2017.
 */
class BotListRow(var bot: BotDbContract.BotsTable.BotEntry, val rootView: View,
                 val botListAdapter: BotListAdapter,
                 val onRowClicked : ((BotDbContract.BotsTable.BotEntry) -> Unit)) : View.OnClickListener {

    companion object {
        private const val REMOVE_BUTTON_TAG = "REMOVE_BUTTON_TAG"

        /**
         * Initialize a bot list row given a bot
         */
        fun fromBot(bot: BotDbContract.BotsTable.BotEntry, layoutInflator: LayoutInflater,
                    botListAdapter: BotListAdapter, onRowClicked : ((BotDbContract.BotsTable.BotEntry) -> Unit)) : BotListRow {

            val view = layoutInflator.inflate(R.layout.layout_bot_list_row, null)

            val botListRow = BotListRow(bot, view, botListAdapter, onRowClicked)
            botListRow.set(bot)
            return botListRow
        }
    }

    val imgAvatar: ImageView

    val tvName: TextView
    val tvGroup: TextView
    val btnRemove: ImageButton

    init {
        imgAvatar = rootView.findViewById(R.id.imgAvatar) as ImageView
        tvName = rootView.findViewById(R.id.tvName) as TextView
        tvGroup = rootView.findViewById(R.id.tvGroup) as TextView
        btnRemove = rootView.findViewById(R.id.btnRemove) as ImageButton

        //click handlers
        rootView.tag = this
        rootView.setOnClickListener(this)

        btnRemove.tag = REMOVE_BUTTON_TAG
        btnRemove.setOnClickListener(this)
    }

    /**
     * Set the row's views to reflect the given bot
     */
    fun set(bot: BotDbContract.BotsTable.BotEntry) {
        this.bot = bot

        tvName.text = bot.displayName()
        tvGroup.text = bot.groupName

        if (bot.avatarUrl != null && !TextUtils.isEmpty(bot.avatarUrl)) {
            Glide.with(rootView.context).load(bot.avatarUrl).into(imgAvatar)
        } else {
            Glide.with(rootView.context).load(R.drawable.ic_android_black_48dp).into(imgAvatar)
        }
    }

    private fun onRemoveButtonClicked() {
        RemoveBotDialogWrapper(bot, botListAdapter, rootView.context).show()
    }

    override fun onClick(view: View?) {
        when (view?.tag) {
            this -> onRowClicked(bot)
            REMOVE_BUTTON_TAG -> onRemoveButtonClicked()
        }
    }

}
