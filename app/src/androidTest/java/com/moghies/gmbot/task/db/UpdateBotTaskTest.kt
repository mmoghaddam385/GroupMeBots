package com.moghies.gmbot.task.db

import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.runner.AndroidJUnit4
import com.moghies.gmbot.db.BotDbContract
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by mmogh on 6/25/2017.
 */
@RunWith(AndroidJUnit4::class)
class UpdateBotTaskTest : DbTaskTestBase() {

    @Before
    override fun init() {
        super.init()

        dbHelper.writableDatabase.use { db ->
            db.insert(BotDbContract.BotsTable.TABLE_NAME, null, getContentValues(BotDbContract.BotsTable.BotEntry.GENERIC_BOT))
        }
    }

    @Test
    fun testUpdate() {
        val updatedBot = BotDbContract.BotsTable.BotEntry.GENERIC_BOT.copy(
                name = "updated",
                groupName = "different"
        )

        UpdateBotTask(updatedBot, getTargetContext()).execute().get()

        assertBotExists(updatedBot)
    }
}
