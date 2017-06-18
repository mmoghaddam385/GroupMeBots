package com.moghies.gmbot

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.task.db.SelectBotsTask

/**
 * This list adapter populates bots from SQLite storage
 *
 * Created by mmogh on 6/18/2017.
 */
class BotListAdapter : BaseAdapter() {

    var bots: List<BotDbContract.BotsTable.BotEntry> = mutableListOf()

    /**
     * Use this function to load bots from sqlite storage
     */
    fun loadFromStorage(context: Context) {
        SelectBotsTask(context = context, onComplete = { bots ->
            this.bots = bots
            this.notifyDataSetChanged()
        }).execute()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            updateView(convertView.tag as ListItemViewBag, bots[position])
            convertView
        } else {
            createView(bots[position], parent!!)
        }
    }

    override fun getItem(position: Int): Any {
        return bots[position]
    }

    override fun getItemId(position: Int): Long {
        return bots[position].hashCode().toLong()
    }

    override fun getCount(): Int {
        return bots.size
    }

    private fun updateView(viewBag: ListItemViewBag, bot: BotDbContract.BotsTable.BotEntry) {
        viewBag.tvName.text = if (!TextUtils.isEmpty(bot.name)) bot.name else bot.id
        viewBag.tvGroup.text = bot.groupName

        if (bot.avatarUrl != null) {
            //TODO: This
        } else {
            viewBag.imgAvatar.setImageResource(R.drawable.ic_android_black_48dp)
        }
    }

    private fun createView(bot: BotDbContract.BotsTable.BotEntry, parent: ViewGroup): View {
        val layoutInflator = LayoutInflater.from(parent.context)
        val view = layoutInflator.inflate(R.layout.layout_bot_list_row, null)

        view.tag = ListItemViewBag(
                imgAvatar = view.findViewById(R.id.imgAvatar) as ImageView,
                tvName = view.findViewById(R.id.tvName) as TextView,
                tvGroup = view.findViewById(R.id.tvGroup) as TextView,
                btnRemove = view.findViewById(R.id.btnRemove) as ImageButton
        )

        updateView(view.tag as ListItemViewBag, bot)
        return view
    }

    /**
     * Simple class to hold the views in the list item
     */
    private data class ListItemViewBag(val imgAvatar: ImageView, val tvName: TextView,
                                       val tvGroup: TextView, val btnRemove: ImageButton)

}