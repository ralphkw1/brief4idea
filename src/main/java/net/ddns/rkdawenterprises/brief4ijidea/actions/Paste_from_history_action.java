package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import net.ddns.rkdawenterprises.brief4ijidea.Actions_component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Paste_from_history_action
        extends Plugin_action
{
    public Paste_from_history_action( String text,
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
        Actions_component.do_action( "PasteMultiple", e );

        Editor editor = e.getData( CommonDataKeys.EDITOR );
        if( editor == null ) return;

        Actions_component.stop_all_marking_modes( editor, false );
    }
}
