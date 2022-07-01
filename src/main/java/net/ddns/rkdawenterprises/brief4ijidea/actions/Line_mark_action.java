
package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.toggle_line_marking_mode;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Line_mark_action
        extends Plugin_action
{
    public Line_mark_action( String text,
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
        if( editor == null ) return;

        toggle_line_marking_mode( editor );
    }
}
