@file:Suppress("ClassName",
               "FunctionName",
               "LocalVariableName",
               "unused",
               "RedundantSemicolon",
               "ComponentNotRegistered",
               "PrivatePropertyName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import net.ddns.rkdawenterprises.brief4ijidea.get_bottom_of_window_line_number
import net.ddns.rkdawenterprises.brief4ijidea.get_editor_content_visible_area
import net.ddns.rkdawenterprises.brief4ijidea.scroll_lines

class Line_to_bottom_of_window_action(text: String? = null,
                                      description: String? = null) : Plugin_action(text,
                                                                                   description)
{
    init
    {
        isEnabledInModalContext = true;
    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent)
    {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return;

        val caret_position_visual = editor.caretModel.visualPosition;
        val lines_to_bottom_of_window = get_bottom_of_window_line_number(editor) - caret_position_visual.line;

        scroll_lines(editor,
                     lines_to_bottom_of_window);
    }
}
