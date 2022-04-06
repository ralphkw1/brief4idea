
package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import net.ddns.rkdawenterprises.brief4ijidea.Actions_component;
import net.ddns.rkdawenterprises.brief4ijidea.Column_marking_component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Cut_to_scrap_action
        extends Plugin_action
{
    public Cut_to_scrap_action( String text,
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
            Actions_component.do_action( "EditorCut",
                                         e );

            if( editor != null )
            {
                Actions_component.stop_all_marking_modes( editor );
            }
        }
        else
        {
            if( editor != null )
            {
                Column_marking_component.cut_to_scrap( editor );
                Actions_component.stop_all_marking_modes( editor );
            }
        }
    }
}
