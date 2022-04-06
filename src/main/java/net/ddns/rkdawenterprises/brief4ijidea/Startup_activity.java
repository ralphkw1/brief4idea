
package net.ddns.rkdawenterprises.brief4ijidea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class Startup_activity
        implements StartupActivity
{
    private boolean m_initialized = false;

    @Override
    public void runActivity( @NotNull Project project )
    {
        if( m_initialized && State_component.enabled() )
        {
            System.out.println( "brief4ijidea.Startup_activity.runActivity: Project scope already initialized." );
        }

        if( m_initialized ) return;

        m_initialized = true;

        State_component.get_instance()
                           .initialize();
    }
}
