@file:Suppress("RedundantSemicolon",
               "ComponentNotRegistered",
               "unused",
               "ClassName",
               "FunctionName",
               "LocalVariableName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.ide.bookmarks.BookmarkManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.testFramework.LightVirtualFile

/**
 * Small tweak to the existing "toggle bookmark" action to conform to the Brief compatible functionality (toggle vs. drop).
 */
open class Drop_bookmark_action(type: Int,
                                text: String? = null,
                                description: String? = null) : Plugin_action(text,
                                                                             description)
{
    private val m_mnemonic: Char = ('0'.code + type).toChar();

    init
    {
        isEnabledInModalContext = true;
    }

    private val AnActionEvent.bookmark_manager
        get() = project?.let { BookmarkManager.getInstance(it); }

    /**
     * Updates the state of the action. Default implementation does nothing.
     * Override this method to provide the ability to dynamically change action's
     * state and(or) presentation depending on the context (For example
     * when your action state depends on the selection you can check for
     * selection and change the state accordingly).
     *
     *
     *
     * This method can be called frequently, and on UI thread.
     * This means that this method is supposed to work really fast,
     * no real work should be done at this phase. For example, checking selection in a tree or a list,
     * is considered valid, but working with a file system or PSI (especially resolve) is not.
     * If you cannot determine the state of the action fast enough,
     * you should do it in the [.actionPerformed] method and notify
     * the user that action cannot be executed if it's the case.
     *
     *
     *
     * If the action is added to a toolbar, its "update" can be called twice a second, but only if there was
     * any user activity or a focus transfer. If your action's availability is changed
     * in absence of any of these events, please call `ActivityTracker.getInstance().inc()` to notify
     * action subsystem to update all toolbar actions when your subsystem's determines that its actions' visibility might be affected.
     *
     * @param e Carries information on the invocation place and data available
     */
    override fun update(e: AnActionEvent)
    {
        val project = e.project;
        val manager = e.bookmark_manager;
        e.presentation.isEnabledAndVisible = (manager != null) && (project != null);
    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent)
    {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: e.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE);
        if((editor == null) || (editor.isOneLineMode)) return;
        editor.project ?: e.project ?: return;
        val manager = e.bookmark_manager ?: return;

        var virtual_file = FileDocumentManager.getInstance()
            .getFile(editor.document);
        if(virtual_file is LightVirtualFile) return;

        val gutter_logical_line_at_cursor = EditorGutterComponentEx.LOGICAL_LINE_AT_CURSOR.getData(e.dataContext);
        var line_number = gutter_logical_line_at_cursor ?: editor.caretModel.logicalPosition.line;
        var bookmark = manager.findEditorBookmark(editor.document,
                                                         line_number);
        if(virtual_file == null)
        {
            virtual_file = CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext);
            line_number = -1;
            if((bookmark == null) && (virtual_file != null))
            {
                bookmark = manager.findFileBookmark(virtual_file);
            }
        }

        if(bookmark == null)
        {
            if(virtual_file != null)
            {
                manager.setMnemonic(manager.addTextBookmark(virtual_file,
                                                            line_number,
                                                            ""),
                                    m_mnemonic);
            }

            return;
        }

        if(bookmark.mnemonic != m_mnemonic)
        {
            manager.removeBookmark(bookmark);

            if(virtual_file != null)
            {
                manager.setMnemonic(manager.addTextBookmark(virtual_file,
                                                            line_number,
                                                            ""),
                                    m_mnemonic);
            }

            return;
        }
    }
}

class Drop_bookmark_10_action(text: String,
                              description: String) : Drop_bookmark_action(0,
                                                                          text,
                                                                          description)

class Drop_bookmark_1_action(text: String,
                             description: String) : Drop_bookmark_action(1,
                                                                         text,
                                                                         description)

class Drop_bookmark_2_action(text: String,
                             description: String) : Drop_bookmark_action(2,
                                                                         text,
                                                                         description)

class Drop_bookmark_3_action(text: String,
                             description: String) : Drop_bookmark_action(3,
                                                                         text,
                                                                         description)

class Drop_bookmark_4_action(text: String,
                             description: String) : Drop_bookmark_action(4,
                                                                         text,
                                                                         description)

class Drop_bookmark_5_action(text: String,
                             description: String) : Drop_bookmark_action(5,
                                                                         text,
                                                                         description)

class Drop_bookmark_6_action(text: String,
                             description: String) : Drop_bookmark_action(6,
                                                                         text,
                                                                         description)

class Drop_bookmark_7_action(text: String,
                             description: String) : Drop_bookmark_action(7,
                                                                         text,
                                                                         description)

class Drop_bookmark_8_action(text: String,
                             description: String) : Drop_bookmark_action(8,
                                                                         text,
                                                                         description)

class Drop_bookmark_9_action(text: String,
                             description: String) : Drop_bookmark_action(9,
                                                                         text,
                                                                         description)
