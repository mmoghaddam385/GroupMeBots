package com.moghies.gmbot

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.moghies.gmbot.db.BotDbContract
import com.moghies.gmbot.task.db.SelectBotsTask
import com.moghies.gmbot.view.BotListRow

/**
 * This list adapter populates bots from SQLite storage
 *
 * Created by mmogh on 6/18/2017.
 */
class BotListAdapter : BaseAdapter() {

    private var bots: MutableList<BotDbContract.BotsTable.BotEntry> = mutableListOf()

    /**
     * Use this function to load bots from sqlite storage
     */
    fun loadFromStorage(context: Context) {
        Log.i(this.javaClass.name, "Loading bots from storage")
        SelectBotsTask(context = context, onComplete = { bots ->
            this.bots = bots
            this.notifyDataSetChanged()
        }).execute()
    }

    /**
     * remove a bot from the list view
     */
    fun removeBot(bot: BotDbContract.BotsTable.BotEntry) {
        if (bots.remove(bot)) {
            notifyDataSetChanged() // only notify change if a bot was actually removed
        }
    }

    /**
     * add a bot to the list view
     */
    fun addBot(bot: BotDbContract.BotsTable.BotEntry) {
        bots.add(bot)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val bot = bots[position]

        if (convertView != null) {

            // view already exists, update and return it
            (convertView.tag as BotListRow).set(bot)
            return convertView
        } else {

            // view does not exist yet, create and return it
            val inflater = LayoutInflater.from(parent!!.context)
            return BotListRow.fromBot(bot, inflater, this).rootView
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

}