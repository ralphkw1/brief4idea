@file:Suppress("RedundantSemicolon",
               "ComponentNotRegistered",
               "unused",
               "ClassName",
               "FunctionName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import net.ddns.rkdawenterprises.brief4ijidea.do_action
import net.ddns.rkdawenterprises.brief4ijidea.get_bottom_of_window_line_number
import net.ddns.rkdawenterprises.brief4ijidea.get_top_of_window_line_number
import net.ddns.rkdawenterprises.brief4ijidea.scroll_lines

class Center_line_in_window_action(text: String?,
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
        // "EditorScrollToCenter" does not currently work very well.

        val editor = e.getData(CommonDataKeys.EDITOR) ?: return;

        val top = get_top_of_window_line_number(editor);
        val bottom = get_bottom_of_window_line_number(editor);
        val center = top + ((bottom - top) / 2);

        val caret_position_visual = editor.caretModel.visualPosition;
        val lines_to_center_of_window = caret_position_visual.line - center;

        scroll_lines(editor, -lines_to_center_of_window);
    }
}