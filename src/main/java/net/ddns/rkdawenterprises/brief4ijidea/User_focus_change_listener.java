package net.ddns.rkdawenterprises.brief4ijidea;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx;
import com.intellij.openapi.editor.ex.FocusChangeListener;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

public class User_focus_change_listener
        implements Disposable
{
    User_focus_change_listener( @NotNull Disposable disposable )
    {
        Disposer.register( disposable,
                           this );

        EditorEventMulticasterEx editor_event_multicaster =
                (EditorEventMulticasterEx)EditorFactory.getInstance()
                                                       .getEventMulticaster();

        editor_event_multicaster.addFocusChangeListener( new FocusChangeListener()
                                                         {
                                                             @Override
                                                             public void focusGained( @NotNull Editor editor )
                                                             {
                                                                 Actions_component.editor_gained_focus( editor );
                                                             }

                                                             @Override
                                                             public void focusLost( @NotNull Editor editor )
                                                             {
                                                                 Actions_component.editor_lost_focus( editor );
                                                             }
                                                         },
                                                         disposable );
    }

    /**
     * Usually not invoked directly, see class javadoc.
     */
    @Override
    public void dispose() {}
}
