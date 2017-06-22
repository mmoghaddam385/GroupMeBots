package com.moghies.gmbot.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract


/**
 * A simple [Fragment] subclass.
 */
class BotInfoFragment(bot: BotDbContract.BotsTable.BotEntry) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_bot_info, container, false)
    }

}// Required empty public constructor
