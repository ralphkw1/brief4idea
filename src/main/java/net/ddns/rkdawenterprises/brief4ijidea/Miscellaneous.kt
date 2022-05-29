@file:Suppress("FunctionName",
               "LocalVariableName",
               "PrivatePropertyName",
               "HardCodedStringLiteral",
               "unused",
               "RedundantSemicolon",
               "UsePropertyAccessSyntax",
               "KDocUnresolvedReference")

package net.ddns.rkdawenterprises.brief4ijidea

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.messages.MessagesService
import com.intellij.util.SystemProperties
import com.intellij.util.ui.UIUtil
import java.awt.Component
import java.awt.Rectangle
import java.util.*
import javax.swing.Icon

fun virtual_space_setting_warning(editor: Editor)
{
    val do_not_show_virtual_space_setting_dialog = State_component.get_instance()
        .get_do_not_show_virtual_space_setting_dialog();
    if(!do_not_show_virtual_space_setting_dialog)
    {
        val editor_settings = editor.settings;
        if(!editor_settings.isVirtualSpace)
        {
            ApplicationManager.getApplication()
                .invokeLater {
                    warning_message("Change Settings for this Command",
                                    Messages.message("you.must.enable.settings.editor.general.virtual.space.after.the.end.of.line.for.some.commands.right.side.of.window.and.column.marking.mode.to.work.properly"),
                                    object : DialogWrapper.DoNotAskOption.Adapter()
                                    {
                                        /**
                                         * Save the state of the checkbox in the settings, or perform some other related action.
                                         * This method is called right after the dialog is [closed][.close].
                                         * <br></br>
                                         * Note that this method won't be called in the case when the dialog is closed by [Cancel][.CANCEL_EXIT_CODE]
                                         * if [saving the choice on cancel is disabled][.shouldSaveOptionsOnCancel] (which is by default).
                                         *
                                         * @param isSelected true if user selected "don't show again".
                                         * @param exitCode   the [exit code][.getExitCode] of the dialog.
                                         * @see .shouldSaveOptionsOnCancel
                                         */
                                        /**
                                         * Save the state of the checkbox in the settings, or perform some other related action.
                                         * This method is called right after the dialog is [closed][.close].
                                         * <br></br>
                                         * Note that this method won't be called in the case when the dialog is closed by [Cancel][.CANCEL_EXIT_CODE]
                                         * if [saving the choice on cancel is disabled][.shouldSaveOptionsOnCancel] (which is by default).
                                         *
                                         * @param isSelected true if user selected "don't show again".
                                         * @param exitCode   the [exit code][.getExitCode] of the dialog.
                                         * @see .shouldSaveOptionsOnCancel
                                         */
                                        override fun rememberChoice(isSelected: Boolean,
                                                                    exitCode: Int)
                                        {
                                            State_component.get_instance()._do_not_show_virtual_space_setting_dialog = isSelected;
                                        }
                                    });
                }
        }
    }
}

fun warning_message(title: String?,
                    message: String,
                    option: DialogWrapper.DoNotAskOption?): String?
{
    val button_name = "OK";

    return Message(title,
                   message)
        .buttons(button_name)
        .default_button(button_name)
        .focused_button(button_name)
        .do_not_ask(option)
        .as_warning()
        .show();
}

class Message internal constructor(private val title: String?,
                                   private val message: String)
{
    private var m_icon: Icon? = null;
    private var m_do_not_ask_option: DialogWrapper.DoNotAskOption? = null;
    private lateinit var m_buttons: List<String>;
    private var m_default_button_name: String? = null;
    private var m_focused_button_name: String? = null;

    fun icon(icon: Icon?): Message
    {
        m_icon = icon;
        return this;
    }

    fun as_warning(): Message
    {
        m_icon = UIUtil.getWarningIcon();
        return this;
    }

    fun do_not_ask(option: DialogWrapper.DoNotAskOption?): Message
    {
        m_do_not_ask_option = option;
        return this;
    }

    fun buttons(vararg button_names: String): Message
    {
        m_buttons = button_names.toList();
        return this;
    }

    fun default_button(default_button_name: String): Message
    {
        m_default_button_name = default_button_name;
        return this;
    }

    fun focused_button(focused_button_name: String): Message
    {
        m_focused_button_name = focused_button_name;
        return this;
    }

    fun show(project: Project? = null,
             parent_component: Component? = null): String?
    {
        val options = m_buttons.toTypedArray();
        val default_option_index = m_buttons.indexOf(m_default_button_name);
        val focused_option_index = m_buttons.indexOf(m_focused_button_name);
        val result = MessagesService.getInstance()
            .showMessageDialog(project = project,
                               parentComponent = parent_component,
                               message = message,
                               title = title,
                               options = options,
                               defaultOptionIndex = default_option_index,
                               focusedOptionIndex = focused_option_index,
                               icon = m_icon,
                               doNotAskOption = m_do_not_ask_option,
                               alwaysUseIdeaUI = true);

        return if(result < 0) null else m_buttons[result];
    }
}

fun get_editor_content_visible_area(editor: Editor): Rectangle
{
    val model = editor.scrollingModel
    return if(SystemProperties.isTrueSmoothScrollingEnabled()) model.visibleAreaOnScrollingFinished else model.visibleArea;
}

fun to_nearest_visual_line_base(editor: Editor,
                                y: Int): Int
{
    val transformed = editor.visualLineToY(editor.yToVisualLine(y))
    return if(y > transformed && y < transformed + editor.lineHeight) transformed else y
}

/**
 * Scrolls the given editor the given number of lines up or down.
 *
 * @param editor The editor to scroll.
 * @param lines Negative value scrolls to a decreasing line number. Positive value scrolls to an increasing line number.
 */
fun scroll_lines(editor: Editor,
                 lines: Int)
{
    val lineHeight: Int = editor.lineHeight
    val visibleArea = get_editor_content_visible_area(editor)
    val y = visibleArea.y - (lineHeight * lines);
    val scroll_offset = to_nearest_visual_line_base(editor,
                                                    y);
    editor.scrollingModel
        .scrollVertically(scroll_offset);
}

fun has_selection(editor: Editor): Boolean
{
    return editor.selectionModel
        .hasSelection();
}

fun capitalize_character_at_index(string: String,
                                  index: Int): String?
{
    return string.substring(0,
                            index) +
            string.substring(index,
                             index + 1)
                .uppercase(Locale.getDefault()) +
            string.substring(index + 1)
}
