@file:Suppress("RedundantSemicolon",
               "ComponentNotRegistered",
               "unused",
               "ClassName",
               "FunctionName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import net.ddns.rkdawenterprises.brief4ijidea.do_action

class Write_action(text: String?,
                   description: String?) : Plugin_action(text,
                                                         description)
{
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent)
    {
        do_action("SaveDocument", e);
    }
}