
package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import net.ddns.rkdawenterprises.brief4ijidea.Actions_component;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Swap_mark_with_scrap_action
        extends Plugin_action
{
    public Swap_mark_with_scrap_action( String text,
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
        Project project = e.getData( CommonDataKeys.PROJECT );
        PsiFile file = e.getData( CommonDataKeys.PSI_FILE );
        if( ( editor == null ) || ( project == null ) || ( file == null ) ) return;

        if( !Actions_component.has_selection( editor ) ) return;

        if( !CopyPasteManager.getInstance()
                             .areDataFlavorsAvailable( DataFlavor.stringFlavor ) ) return;

        String text = CopyPasteManager.getInstance()
                                      .getContents( DataFlavor.stringFlavor );

        if( ( text == null ) || !( text.length() > 0 ) ) return;

        Actions_component.do_action( "$Cut",
                                     e );

        Actions_component.stop_all_marking_modes( editor );

        WriteCommandAction.runWriteCommandAction( project,
                                                  e.getPresentation()
                                                   .getText(),
                                                  null,
                                                  () -> editor.getDocument()
                                                              .insertString( editor.getCaretModel()
                                                                                   .getCurrentCaret()
                                                                                   .getOffset(),
                                                                             text ),
                                                  file );
    }
}
