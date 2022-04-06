
package net.ddns.rkdawenterprises.brief4ijidea;

import com.intellij.openapi.Disposable;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class Settings_UI_component
        implements Disposable
{
    private final JPanel myMainPanel;

    private final JBCheckBox m_enabled = new JBCheckBox( "Enable or disable plugin." );
    private final JBCheckBox m_paste_lines_at_home = new JBCheckBox( "Paste whole lines at home position." );
    private final JBCheckBox m_use_brief_home = new JBCheckBox( "Use Brief home key functionality." );
    private final JBCheckBox m_use_relative_bookmarks = new JBCheckBox( "Use project relative bookmarks." );
    private final JBCheckBox m_check_active_keymap_is_brief = new JBCheckBox( "Startup check active keymap is Brief." );
    private final JBCheckBox m_exit_only_closes_editor = new JBCheckBox( "\"Exit\" command only closes active editor." );
    private final JBCheckBox m_write_all_and_exit_closes_IDEA = new JBCheckBox( "\"Write all and exit\" command closes the IDEA." );
    private final JBCheckBox m_do_not_show_virtual_space_setting_dialog = new JBCheckBox( "Do not show virtual space setting dialog again." );

    public Settings_UI_component()
    {
        m_enabled.setToolTipText( "Enable the Brief Editor Emulator functionality. Uncheck to disable the plugin." );
        m_paste_lines_at_home.setToolTipText( "If the item in the scrap history buffer being pasted is a full line (ends with a line termination), then paste it at the beginning of the current line. Uncheck to paste at the current cursor location." );
        m_use_brief_home.setToolTipText( "Use the home key functionality as documented in Brief. This disables the normal \"smart\" home functionality. Uncheck to restore \"smart\" home key functionality, but still maintain Brief \"home-home-home\" key functionality." );
        m_use_relative_bookmarks.setToolTipText( "Bookmark URI are stored and displayed relative to project workspace folder. Uncheck to store and display bookmarks with global scope. Changing this configuration may delete existing stored bookmarks." );
        m_check_active_keymap_is_brief.setToolTipText( "Initial check for active keymap is set to use the included Brief keymap at startup. Uncheck this if you modify the keymap, which makes a copy of the default Brief keymap, because startup will configure default Brief keymap as active keymap if this is checked." );
        m_exit_only_closes_editor.setToolTipText( "\"Exit\" command will close the currently active editor, not the IDEA. Original Brief functionality would close the application. Uncheck this if you want the original functionality." );
        m_write_all_and_exit_closes_IDEA.setToolTipText( "\"Write all and exit\" command will close the IDEA. This is original Brief functionality. Uncheck this if you want to close only the editors, but keep the IDEA running." );
        m_do_not_show_virtual_space_setting_dialog.setToolTipText( "Disables showing of the virtual space setting dialog again when initiating the \"Right side of window\" action. Uncheck if you want to see this dialog again." );

        myMainPanel = FormBuilder.createFormBuilder()
                                 .addComponent( m_enabled,
                                                1 )
                                 .addComponent( m_paste_lines_at_home,
                                                1 )
                                 .addComponent( m_use_brief_home,
                                                1 )
                                 .addComponent( m_use_relative_bookmarks,
                                                1 )
                                 .addComponent( m_check_active_keymap_is_brief,
                                                1 )
                                 .addComponent( m_exit_only_closes_editor,
                                                1 )
                                 .addComponent( m_write_all_and_exit_closes_IDEA,
                                                1 )
                                 .addComponent( m_do_not_show_virtual_space_setting_dialog,
                                                1 )
                                 .addComponentFillVertically( new JPanel(),
                                                              0 )
                                 .getPanel();
    }

    /**
     * Usually not invoked directly, see class javadoc.
     */
    @Override
    public void dispose() { }

    public JPanel getPanel()
    {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent()
    {
        return m_enabled;
    }

    public boolean get_enabled()
    {
        return m_enabled.isSelected();
    }
    public void set_enabled( boolean check )
    {
        m_enabled.setSelected( check );
    }

    public boolean get_paste_lines_at_home()
    {
        return m_paste_lines_at_home.isSelected();
    }
    public void set_paste_lines_at_home( boolean check )
    {
        m_paste_lines_at_home.setSelected( check );
    }

    public boolean get_use_brief_home()
    {
        return m_use_brief_home.isSelected();
    }
    public void set_use_brief_home( boolean check )
    {
        m_use_brief_home.setSelected( check );
    }

    public boolean get_use_relative_bookmarks()
    {
        return m_use_relative_bookmarks.isSelected();
    }
    public void set_use_relative_bookmarks( boolean check )
    {
        m_use_relative_bookmarks.setSelected( check );
    }

    public boolean get_check_active_keymap_is_brief()
    {
        return m_check_active_keymap_is_brief.isSelected();
    }
    public void set_check_active_keymap_is_brief( boolean check ) { m_check_active_keymap_is_brief.setSelected( check ); }

    public boolean get_exit_only_closes_editor()
    {
        return m_exit_only_closes_editor.isSelected();
    }
    public void set_exit_only_closes_editor( boolean check ) { m_exit_only_closes_editor.setSelected( check ); }

    public boolean get_write_all_and_exit_closes_IDEA()
    {
        return m_write_all_and_exit_closes_IDEA.isSelected();
    }
    public void set_write_all_and_exit_closes_IDEA( boolean check ) { m_write_all_and_exit_closes_IDEA.setSelected( check ); }

    public boolean get_do_not_show_virtual_space_setting_dialog()
    {
        return m_do_not_show_virtual_space_setting_dialog.isSelected();
    }
    public void set_do_not_show_virtual_space_setting_dialog( boolean check ) { m_do_not_show_virtual_space_setting_dialog.setSelected( check ); }
}
