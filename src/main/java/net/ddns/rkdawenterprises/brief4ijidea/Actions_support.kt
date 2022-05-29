@file:Suppress("FunctionName",
               "LocalVariableName")

package net.ddns.rkdawenterprises.brief4ijidea

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project

fun do_action(action_ID: String,
              an_action_event: AnActionEvent)
{
    val action_manager_ex = ActionManagerImpl.getInstanceEx();
    val action = action_manager_ex.getAction(action_ID);
    ActionUtil.performActionDumbAwareWithCallbacks(action,
                                                   an_action_event,
                                                   an_action_event.dataContext);
}

fun do_action(action_ID: String,
              an_action_event: AnActionEvent,
              an_action: AnAction)
{
    if(!should_promote(an_action, an_action_event.dataContext)) return;
    val action_manager_ex = ActionManagerImpl.getInstanceEx();
    val action = action_manager_ex.getAction(action_ID);

    ActionUtil.performActionDumbAwareWithCallbacks(action,
                                                   an_action_event,
                                                   an_action_event.dataContext);
}

fun get_undo_manager(project: Project?,
                     dataContext: DataContext): UndoManager
{
    return if(project != null && !project.isDefault) UndoManager.getInstance(project) else UndoManager.getGlobalInstance();
}

fun stop_all_marking_modes(editor: Editor,
                           remove_selection: Boolean)
{
    Marking_component.stop_marking_mode(editor,
                                        remove_selection)
    Line_marking_component.stop_line_marking_mode(editor,
                                                  remove_selection)
    Column_marking_component.stop_column_marking_mode(editor,
                                                      remove_selection)
    State_component.status_bar_message(null)
    if(remove_selection)
    {
        if(has_selection(editor))
        {
            editor.caretModel
                .removeSecondaryCarets()
            editor.selectionModel
                .removeSelection()
        }
    }
}

fun stop_all_marking_modes(editor: Editor)
{
    stop_all_marking_modes(editor,
                           true)
}

fun validate_position(editor: Editor,
                      position: LogicalPosition): LogicalPosition?
{
    return editor.offsetToLogicalPosition(editor.logicalPositionToOffset(position))
}

fun editor_gained_focus(editor: Editor)
{
    stop_all_marking_modes(editor,
                           false)
}

fun editor_lost_focus(editor: Editor)
{
    stop_all_marking_modes(editor,
                           false)
}

fun toggle_marking_mode(editor: Editor)
{
    Line_marking_component.stop_line_marking_mode(editor,
                                                  true)
    Column_marking_component.stop_column_marking_mode(editor,
                                                      true)
    Marking_component.toggle_marking_mode(editor)
}

fun toggle_line_marking_mode(editor: Editor)
{
    Marking_component.stop_marking_mode(editor,
                                        true)
    Column_marking_component.stop_column_marking_mode(editor,
                                                      true)
    Line_marking_component.toggle_line_marking_mode(editor)
}

fun toggle_column_marking_mode(editor: Editor)
{
    Marking_component.stop_marking_mode(editor,
                                        true)
    Line_marking_component.stop_line_marking_mode(editor,
                                                  true)
    Column_marking_component.toggle_column_marking_mode(editor)
}
