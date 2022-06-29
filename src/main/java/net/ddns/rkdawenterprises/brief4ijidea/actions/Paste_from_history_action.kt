@file:Suppress("RedundantSemicolon",
               "ComponentNotRegistered",
               "unused",
               "ClassName",
               "FunctionName",
               "HardCodedStringLiteral",
               "PrivatePropertyName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import net.ddns.rkdawenterprises.brief4ijidea.do_action
import net.ddns.rkdawenterprises.brief4ijidea.stop_all_marking_modes
import java.util.concurrent.atomic.AtomicBoolean

class Paste_from_history_action(text: String?,
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

        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        dialog_is_open.set(true);
        do_action("PasteMultiple",
                  e)
        dialog_is_open.set(false);
        
        stop_all_marking_modes(editor,
                               false)
    }
}