package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import net.ddns.rkdawenterprises.brief4ijidea.Miscellaneous;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Right_side_of_window_action
        extends Plugin_action
{
    public Right_side_of_window_action( String text,
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

        Miscellaneous.virtual_space_setting_warning( editor );

        Rectangle visible_area = Miscellaneous.get_editor_content_visible_area( editor );

        int max_X = visible_area.x + visible_area.width - ( (EditorImpl)editor ).getScrollPane()
                                                                                .getVerticalScrollBar()
                                                                                .getWidth();
        Point cursor_point = editor.visualPositionToXY( editor.getCaretModel()
                                                              .getVisualPosition() );
        Point window_right_at_line_point = new Point( max_X, cursor_point.y );

        VisualPosition window_right_at_line_visual_position = editor.xyToVisualPosition( window_right_at_line_point );

        editor.getCaretModel().moveToVisualPosition( window_right_at_line_visual_position );
    }
}
