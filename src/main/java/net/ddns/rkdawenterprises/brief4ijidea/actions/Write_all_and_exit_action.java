package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import net.ddns.rkdawenterprises.brief4ijidea.State_component;
import org.jetbrains.annotations.NotNull;

import static net.ddns.rkdawenterprises.brief4ijidea.Actions_supportKt.do_action;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Write_all_and_exit_action
        extends Plugin_action
{
    public Write_all_and_exit_action( String text,
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
        do_action( "SaveAll", e );

        if( State_component.get_instance().get_write_all_and_exit_closes_IDEA() )
        {
            do_action( "Exit", e );
        }
        else
        {
            try
            {
                do_action( "CloseAllEditors", e );
            }
            catch( Exception exception ) { System.out.println( exception.getLocalizedMessage() ); }
        }
    }
}
