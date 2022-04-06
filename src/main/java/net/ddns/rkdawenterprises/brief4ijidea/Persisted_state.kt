@file:Suppress("ClassName")

package net.ddns.rkdawenterprises.brief4ijidea

data class Persisted_state
(
    var version: Int = 1,
    /**
     * Enables or disables the Brief Emulator functionality.
     */
    var enabled: Boolean = true,

    /**
     * If the item in the scrap history buffer being pasted is a full line (ends with a line termination), then paste it
     * at the beginning of the current line. Uncheck to paste at the current cursor location.
     */
    var paste_lines_at_home: Boolean = true,

    /**
     * Use home key functionality as documented in Brief. This disables the normal "smart" home functionality. Uncheck
     * to restore "smart" home key functionality, but still maintain Brief "home-home-home" key functionality.
     */
    var use_brief_home: Boolean = true,

    /**
     * Bookmark URI are stored and displayed relative to project workspace folder. Uncheck to store and display
     * globally. Changing this configuration may delete existing stored bookmarks.
     */
    var use_relative_bookmarks: Boolean = true,

    /**
     * The name of the keymap before changing it to Brief, so it can be restored
     * when the plugin is disabled.
     */
    var previous_keymap_name: String? = null,

    /**
     * Initial check for active keymap being set to use the included Brief keymap at startup. Uncheck this if you
     * modify the keymap, which makes a copy of the default Brief keymap, because startup will configure default Brief
     * keymap as active keymap if this is checked.
     */
    var check_active_keymap_is_brief: Boolean = true,

    /**
     * "Exit" command will close the currently active editor, not the IDEA. Original Brief functionality would close
     * the application. Uncheck this if you want the original functionality.
     */
    var exit_only_closes_editor: Boolean = true,

    /**
     * "Write all and exit" command will close the IDEA. This is original Brief functionality. Uncheck this if you
     * want to close only the editors, but keep the IDEA running.
     */
    var write_all_and_exit_closes_IDEA: Boolean = true,

    /**
     * Disables showing of the virtual space setting dialog again when initiating the "Right side of window" action.
     * Uncheck if you want to see this dialog again.
     */
    var do_not_show_virtual_space_setting_dialog: Boolean = false
)