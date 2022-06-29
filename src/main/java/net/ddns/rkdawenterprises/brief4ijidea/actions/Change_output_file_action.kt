@file:Suppress("RedundantSemicolon",
               "ComponentNotRegistered",
               "unused",
               "ClassName",
               "FunctionName",
               "HardCodedStringLiteral",
               "PrivatePropertyName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import net.ddns.rkdawenterprises.brief4ijidea.do_action
import java.util.concurrent.atomic.AtomicBoolean

class Change_output_file_action(text: String?,
                                description: String?) : Plugin_action(text,
                                                             description)
{
    // Without this, remote robot may cause the dialog to open multiple times.
    private val dialog_is_open = AtomicBoolean(false);

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent)
    {
        if(dialog_is_open.get()) return;

        dialog_is_open.set(true);
        do_action("RenameFile", e);
        dialog_is_open.set(false);
    }
}