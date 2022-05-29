
package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import net.ddns.rkdawenterprises.brief4ijidea.Actions_component;
import net.ddns.rkdawenterprises.brief4ijidea.Messages;
import org.jetbrains.annotations.NotNull;

import static net.ddns.rkdawenterprises.brief4ijidea.Actions_supportKt.toggle_column_marking_mode;
import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.virtual_space_setting_warning;
import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.warning_message;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Column_mark_action
        extends Plugin_action
{
    public Column_mark_action( String text,
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
        final Editor editor = e.getData( CommonDataKeys.EDITOR );
        if( editor == null ) return;

        final Project project = e.getData( CommonDataKeys.PROJECT );
        if( ( project == null ) || ( editor.getProject() == null ) )
        {
            warning_message( null,
                             Messages.message( "column.marking.mode.currently.uses.highlightmanager.which.requires.a.project.to.obtain.an.instance" ),
                             null );
            return;
        }

        virtual_space_setting_warning( editor );

        toggle_column_marking_mode( editor );
    }
}
