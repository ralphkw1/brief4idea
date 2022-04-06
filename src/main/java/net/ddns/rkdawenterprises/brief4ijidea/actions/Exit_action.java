package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorWithProviderComposite;
import com.intellij.openapi.fileEditor.impl.EditorsSplitters;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.impl.ProjectImpl;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.newvfs.impl.VirtualFileImpl;
import com.intellij.psi.PsiFile;
import net.ddns.rkdawenterprises.brief4ijidea.Actions_component;
import net.ddns.rkdawenterprises.brief4ijidea.State_component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Exit_action
        extends Plugin_action
{
    public Exit_action( String text,
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
        /// If unable to determine file modification status, ask to save anyway.
        boolean is_modified = true;
        String file_name = null;

        final ProjectImpl project = (ProjectImpl)e.getData( CommonDataKeys.PROJECT );
        final PsiFile a_PSI_file = e.getData( CommonDataKeys.PSI_FILE );
        if( ( project != null ) && ( a_PSI_file != null ) )
        {
            final VirtualFileImpl virtual_file = (VirtualFileImpl)a_PSI_file.getVirtualFile();
            if( virtual_file != null )
            {
                file_name = virtual_file.getName();
                if( !is_file_modified( project,
                                       virtual_file ) ) is_modified = false;
            }
        }

        if( is_modified )
        {
            if( Messages.showYesNoDialog( "Write changes to \"" + ( file_name != null ? file_name : "file" ) +
                                                  "\" before closing, if you want them externally accessible.",
                                          "Write Changes?",
                                          Messages.getQuestionIcon() ) == Messages.YES )
            {
                Actions_component.do_action( "SaveDocument", e );
            }
        }

        if( State_component.get_instance()
                           .get_exit_only_closes_editor() )
        {
            Actions_component.do_action( "CloseEditor", e );
        }
        else
        {
            Actions_component.do_action( "Exit", e );
        }
    }

    public static boolean is_file_modified( ProjectImpl project,
                                            VirtualFileImpl file )
    {
        FileEditorManagerImpl file_editor_manager = (FileEditorManagerImpl)FileEditorManager.getInstance( project );
        List<EditorWithProviderComposite> result = new ArrayList<>();
        Set<EditorsSplitters> all = file_editor_manager.getAllSplitters();
        for( EditorsSplitters each : all )
        {
            result.addAll( each.findEditorComposites( file ) );
        }

        boolean modified = false;
        for( EditorWithProviderComposite composite : result )
        {
            modified |= composite.isModified();
        }

        return modified;
    }
}
