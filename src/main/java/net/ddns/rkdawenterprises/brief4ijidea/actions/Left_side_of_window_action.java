package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.get_editor_content_visible_area;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Left_side_of_window_action
        extends Plugin_action
{
    public Left_side_of_window_action( String text,
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
        if( !( editor instanceof EditorImpl ) ) return;

        Rectangle visible_area = get_editor_content_visible_area( editor );

        Point cursor_point = editor.visualPositionToXY( editor.getCaretModel()
                                                              .getVisualPosition() );

        Point window_left_at_line_point = new Point( visible_area.x, cursor_point.y );

        VisualPosition window_left_at_line_visual_position = editor.xyToVisualPosition( window_left_at_line_point );

        editor.getCaretModel().moveToVisualPosition( window_left_at_line_visual_position );
    }
}
