@file:Suppress("RedundantSemicolon",
               "ComponentNotRegistered",
               "unused",
               "ClassName",
               "FunctionName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import net.ddns.rkdawenterprises.brief4ijidea.do_action

open class Jump_to_bookmark_action(private val type: Int,
                                   text: String? = null,
                                   description: String? = null) : Plugin_action(text,
                                                                                description)
{
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent)
    {
        when(type)
        {
            0 -> do_action("GotoBookmark0", e);
            1 -> do_action("GotoBookmark1", e);
            2 -> do_action("GotoBookmark2", e);
            3 -> do_action("GotoBookmark3", e);
            4 -> do_action("GotoBookmark4", e);
            5 -> do_action("GotoBookmark5", e);
            6 -> do_action("GotoBookmark6", e);
            7 -> do_action("GotoBookmark7", e);
            8 -> do_action("GotoBookmark8", e);
            9 -> do_action("GotoBookmark9", e);
            else -> return;
        }
    }
}

class Jump_to_bookmark_10_action(text: String,
                                 description: String) : Jump_to_bookmark_action(0,
                                                                                text,
                                                                                description)

class Jump_to_bookmark_1_action(text: String,
                                description: String) : Jump_to_bookmark_action(1,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_2_action(text: String,
                                description: String) : Jump_to_bookmark_action(2,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_3_action(text: String,
                                description: String) : Jump_to_bookmark_action(3,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_4_action(text: String,
                                description: String) : Jump_to_bookmark_action(4,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_5_action(text: String,
                                description: String) : Jump_to_bookmark_action(5,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_6_action(text: String,
                                description: String) : Jump_to_bookmark_action(6,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_7_action(text: String,
                                description: String) : Jump_to_bookmark_action(7,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_8_action(text: String,
                                description: String) : Jump_to_bookmark_action(8,
                                                                               text,
                                                                               description)

class Jump_to_bookmark_9_action(text: String,
                                description: String) : Jump_to_bookmark_action(9,
                                                                               text,
                                                                               description)
