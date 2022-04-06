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
import net.ddns.rkdawenterprises.brief4ijidea.Miscellaneous

class Line_to_top_of_window_action(text: String? = null,
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

        val visible_area = Miscellaneous.get_editor_content_visible_area(editor);
        var visible_area_top_line_number = editor.yToVisualLine(visible_area.y);
        if(visible_area.y > editor.visualLineToY(visible_area_top_line_number) &&
            visible_area.y + visible_area.height > editor.visualLineToY(visible_area_top_line_number + 1)
        )
        {
            visible_area_top_line_number++;
        }

        val caret_position_visual = editor.caretModel
            .visualPosition;
        val lines_to_top_of_window = caret_position_visual.line - visible_area_top_line_number;

        Miscellaneous.scroll_lines(editor,
                                   -lines_to_top_of_window);
    }
}
