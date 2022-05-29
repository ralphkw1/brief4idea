@file:Suppress("ClassName",
               "RedundantSemicolon")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareToggleAction
import net.ddns.rkdawenterprises.brief4ijidea.Messages
import net.ddns.rkdawenterprises.brief4ijidea.State_component

class Enabled_toggle_action : DumbAwareToggleAction(Messages.message("action.brief.editor.emulator.text"),
                                                    Messages.message("action.enable.or.disable.brief.editor.emulation.plugin.description"),
                                                    null)
{
    /**
     * Returns the selected (checked, pressed) state of the action.
     *
     * @param e the action event representing the place and context in which the selected state is queried.
     *
     * @return true if the action is selected, false otherwise
     */
    override fun isSelected(e: AnActionEvent): Boolean
    {
        return State_component.enabled();
    }

    /**
     * Sets the selected state of the action to the specified value.
     *
     * @param e     the action event which caused the state change.
     * @param state the new selected state of the action.
     */
    override fun setSelected(e: AnActionEvent,
                             state: Boolean)
    {
        try
        {
            State_component.enable(state);
        }
        catch(ignored: Exception){}
    }
}