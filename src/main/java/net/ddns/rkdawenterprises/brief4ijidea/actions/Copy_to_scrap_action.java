
package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actions.EditorActionUtil;
import net.ddns.rkdawenterprises.brief4ijidea.Actions_component;
import net.ddns.rkdawenterprises.brief4ijidea.Column_marking_component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Copy_to_scrap_action
        extends Plugin_action
{
    public Copy_to_scrap_action( String text,
                                 String description )
    {
        super( text,
               description );
    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed( @NotNull AnActionEvent e )
    {
        Editor editor = e.getData( CommonDataKeys.EDITOR );

        if( !Column_marking_component.is_column_marking_mode() )
        {
            int offset = -1;

            if( ( editor != null ) && !Actions_component.has_selection( editor ) )
            {
                offset = editor.getCaretModel()
                               .getCurrentCaret()
                               .getOffset();
            }

            Actions_component.do_action( "EditorCopy",
                                         e );

            if( ( editor != null ) && ( offset > 0 ) )
            {
                EditorActionUtil.moveCaret( editor.getCaretModel()
                                                  .getCurrentCaret(),
                                            offset,
                                            false );
            }

            if( editor != null )
            {
                Actions_component.stop_all_marking_modes( editor );
            }
        }
        else
        {
            if( editor != null )
            {
                Column_marking_component.copy_to_scrap( editor );
                Actions_component.stop_all_marking_modes( editor );
            }
        }
    }
}
