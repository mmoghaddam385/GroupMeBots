package com.moghies.gmbot.dialog

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.TextView
import com.moghies.gmbot.R
import com.moghies.gmbot.db.BotDbContract

/**
 * Created by mmogh on 7/1/2017.
 */
class ReplaceBotsDialog(val ctx: Context,
                        val bots: List<BotDbContract.BotsTable.BotEntry>,
                        val onComplete: (toReplace: List<BotDbContract.BotsTable.BotEntry>) -> Unit) : DialogFragment(), View.OnClickListener, Animation.AnimationListener {

    private companion object {
        const val ANIMATION_DURATION = 100L
    }

    lateinit private var fadeOutAnim: Animation

    private val botsToReplace = mutableListOf<BotDbContract.BotsTable.BotEntry>()

    private lateinit var tvReplaceBotMessage: TextView

    private lateinit var tvProgress: TextView
    private lateinit var chkRememberChoice: CheckBox
    private var curBotIndex = -1 // start at -1 so [moveToNextBot] works in onCreate
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.layout_replace_bot_dialog, null)
        this.isCancelable = false

        tvReplaceBotMessage = view.findViewById(R.id.tvReplaceBotMessage) as TextView
        tvProgress = view.findViewById(R.id.tvProgress) as TextView
        chkRememberChoice = view.findViewById(R.id.chkRememberChoice) as CheckBox

        if (bots.size == 1) {
            tvProgress.visibility = View.GONE
            chkRememberChoice.visibility = View.GONE
        }

        view.findViewById(R.id.btnReplace).setOnClickListener(this)
        view.findViewById(R.id.btnSkip).setOnClickListener(this)

        moveToNextBot()

        return view
    }

    /**
     * Mark the given bot as to be replaced
     */
    private fun replaceCurBot() {
        // if the remember checkbox is checked, add all remaining bots to replace list
        if (chkRememberChoice.isChecked) {
            botsToReplace.addAll(bots.subList(curBotIndex, bots.size))
            curBotIndex = bots.size
        } else {
            botsToReplace.add(bots[curBotIndex])
        }
    }

    /**
     * Mark the given bot as to be skipped
     */
    private fun skipCurBot() {
        // if the remember checkbox is checked, ignore all the rest of the bots
        if (chkRememberChoice.isChecked) {
            curBotIndex = bots.size
        }
    }

    /**
     * Ask the user about the next bot in the list. <br/>
     * If no more bots are in the list, dismiss the dialog and call [onComplete]
     */
    private fun moveToNextBot() {
        curBotIndex++

        if (curBotIndex < bots.size) {
            tvProgress.text = "${curBotIndex + 1} of ${bots.size}"

            tvReplaceBotMessage.clearAnimation()

            fadeOutAnim = AnimationUtils.loadAnimation(ctx, android.R.anim.fade_out)
            fadeOutAnim.duration = if (curBotIndex == 0) 0 else ANIMATION_DURATION
            fadeOutAnim.setAnimationListener(this)

            tvReplaceBotMessage.animation = fadeOutAnim
        } else {
            onComplete(botsToReplace)
            dismiss()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnReplace -> replaceCurBot()
                R.id.btnSkip -> skipCurBot()
            }

            moveToNextBot()
        }
    }

    override fun onAnimationRepeat(p0: Animation?) {}

    override fun onAnimationStart(p0: Animation?) {}

    override fun onAnimationEnd(anim: Animation?) {
        if (anim != null && anim == fadeOutAnim) {
            tvReplaceBotMessage.text = "'${bots[curBotIndex].displayName()}' already exists, do you want to replace it or skip importing this bot?"

            val fadeInAnim = AnimationUtils.loadAnimation(ctx, android.R.anim.fade_in)
            fadeInAnim.duration = ANIMATION_DURATION

            tvReplaceBotMessage.clearAnimation()
            tvReplaceBotMessage.animation = fadeInAnim
        }
    }
}
