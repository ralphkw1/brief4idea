@file:Suppress("ClassName",
               "FunctionName",
               "LocalVariableName",
               "unused",
               "RedundantSemicolon")

package net.ddns.rkdawenterprises.brief4ijidea

import com.intellij.openapi.actionSystem.KeyboardShortcut
import com.intellij.openapi.keymap.impl.KeymapImpl
import java.awt.Toolkit
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

data class Key_action(val action_ID: String, val second: String?)

class Key_event_to_string
{
    companion object
    {
        /**
         * Creates a JSON string with keyboard key information. Does not support mouse buttons.
         *
         * @return A JSON string array with key information.
         *         For "KEY_PRESSED" or "KEY_RELEASED" it will be in the form of [ID, MODIFIERS, VK_x(without the "VK_" prefix)].
         *         For "KEY_TYPED" it will be in the form of [ID, MODIFIERS, character].
         */
        fun KeyEvent.to_string(): String
        {
            val e: KeyEvent = this;

            val builder = StringBuilder("[");

            when(e.id)
            {
                KeyEvent.KEY_PRESSED -> builder.append("\"<KEY_PRESSED>\"");
                KeyEvent.KEY_TYPED -> builder.append("\"<KEY_TYPED>\"");
                KeyEvent.KEY_RELEASED -> builder.append("\"<KEY_RELEASED>\"");
                else ->
                {
                    builder.append("\"<UNSUPPORTED>\"");
                }
            }

            when(e.id)
            {
                KeyEvent.KEY_PRESSED, KeyEvent.KEY_TYPED, KeyEvent.KEY_RELEASED ->
                {
                    val modifiers = get_modifiers_as_string(e);
                    if(modifiers == null) builder.append(",\"<UNMODIFIED>\"") else builder.append(",\"$modifiers\"");
                }
                else ->
                {
                    builder.append("\"<UNSUPPORTED>\"");
                }
            }

            when(e.id)
            {
                KeyEvent.KEY_PRESSED, KeyEvent.KEY_RELEASED ->
                {
                    val name = get_key_name_as_string(e);
                    builder.append(",\"<$name>\"");
                }

                KeyEvent.KEY_TYPED ->
                {
                    val character = e.keyChar;
                    builder.append(",\"<$character>\"");
                }
            }

            builder.append(']');
            return builder.toString();
        }

        fun KeymapImpl.to_map(): Map<String, Key_action>?
        {
            val k: KeymapImpl = this;

            val map = HashMap<String, Key_action>()

            val action_IDs = k.actionIds;
            for(action_ID in action_IDs)
            {
                val shortcuts = k.getShortcuts(action_ID);
                for(shortcut in shortcuts)
                {
                    if(shortcut.isKeyboard)
                    {
                        val shortcut_string = (shortcut as KeyboardShortcut).to_string();

                        val search_string1 = "{\"K1\":[";
                        val search_string2 = "],\"K2\":[";
                        val start_index1 = shortcut_string.indexOf(search_string1) + search_string1.length;
                        val start_index2 = shortcut_string.indexOf(search_string2);
                        val end_index1 = if(start_index2 != -1) start_index2 else (shortcut_string.length - 2);
                        val end_index2 = shortcut_string.length - 2;

                        val key1 = shortcut_string.substring(start_index1, end_index1).
                            replace("\"","").
                        replace("<KEY_PRESSED>,", "").
                        replace("<KEY_RELEASED>,", "");

                        var key2: String? = null;
                        if(start_index2 != -1)
                        {
                            key2 = shortcut_string.substring(start_index2 + search_string2.length, end_index2).
                            replace("\"","").
                            replace("<KEY_PRESSED>,", "").
                            replace("<KEY_RELEASED>,", "");
                        }

                        map[key1] = Key_action(action_ID,
                                               key2);

                    }
                }
            }

            return if( map.isEmpty() ) null else map;
        }

        /**
         * Creates a JSON string with keyboard shortcut key information.
         *
         * @return A JSON string with key information.
         *         It will be in the form of Kx:[ID, MODIFIERS, VK_y],
         *         where x is 1 or 2 for each of the 2 possible keystrokes, and
         *         y is the key name without the "VK_" prefix.
         *
         */
        fun KeyboardShortcut.to_string(): String
        {
            val k: KeyboardShortcut = this;

            val builder = StringBuilder("{\"K1\":");

            builder.append(get_keystroke_as_string(k.firstKeyStroke));

            if(k.secondKeyStroke != null)
            {
                builder.append(",\"K2\":");
                builder.append(get_keystroke_as_string(k.secondKeyStroke!!));
            }

            builder.append("}");
            return builder.toString();
        }

        /**
         * Creates a JSON string with keystroke key information.
         *
         * @return A JSON string array with key information.
         *         It will be in the form of [ID, MODIFIERS, VK_x(without the "VK_" prefix)].
         *
         */
        private fun get_keystroke_as_string( k: KeyStroke ): String
        {
            val builder = StringBuilder("[");

            val ID = k.keyEventType;

            when(ID)
            {
                KeyEvent.KEY_PRESSED -> builder.append("\"<KEY_PRESSED>\"");
                KeyEvent.KEY_TYPED -> builder.append("\"<KEY_TYPED>\"");
                KeyEvent.KEY_RELEASED -> builder.append("\"<KEY_RELEASED>\"");
                else ->
                {
                    builder.append("\"<UNSUPPORTED>\"");
                }
            }

            when(ID)
            {
                KeyEvent.KEY_PRESSED, KeyEvent.KEY_TYPED, KeyEvent.KEY_RELEASED ->
                {
                    val modifiers = get_modifiers_as_string(k.modifiers and (KeyEvent.ALT_DOWN_MASK or KeyEvent.CTRL_DOWN_MASK or KeyEvent.SHIFT_DOWN_MASK));
                    if(modifiers == null) builder.append(",\"<UNMODIFIED>\"") else builder.append(",\"$modifiers\"");
                }
                else ->
                {
                    builder.append(",\"<UNSUPPORTED>\"");
                }
            }

            when(ID)
            {
                KeyEvent.KEY_PRESSED, KeyEvent.KEY_RELEASED ->
                {
                    val name = get_key_name_as_string(k.keyChar,
                                                      KeyEvent.getKeyText(k.keyCode));
                    builder.append(",\"<$name>\"");
                }

                KeyEvent.KEY_TYPED ->
                {
                    val character = k.keyChar;
                    builder.append(",\"<$character>\"");
                }
            }

            builder.append("]");
            return builder.toString();
        }

        private fun get_modifiers_as_string(e: KeyEvent): String?
        {
            val modifiers = e.modifiersEx and (KeyEvent.ALT_DOWN_MASK or KeyEvent.CTRL_DOWN_MASK or KeyEvent.SHIFT_DOWN_MASK);
            return get_modifiers_as_string(modifiers);
        }

        private fun get_modifiers_as_string(modifiers: Int): String?
        {
            if(modifiers == 0) return null;

            val result = StringBuilder();

            if(modifiers and KeyEvent.ALT_DOWN_MASK != 0) result.append("<${
                Toolkit.getProperty("AWT.alt",
                                    "Alt")
            }>");
            if(modifiers and KeyEvent.CTRL_DOWN_MASK != 0) result.append("<${
                Toolkit.getProperty("AWT.control",
                                    "Control")
            }>");
            if(modifiers and KeyEvent.META_DOWN_MASK != 0) result.append("<${
                Toolkit.getProperty("AWT.meta",
                                    "Meta")
            }>");
            if(modifiers and KeyEvent.SHIFT_DOWN_MASK != 0) result.append("<${
                Toolkit.getProperty("AWT.shift",
                                    "Shift")
            }>");

            return if(result.isNotEmpty()) result.toString() else null;
        }

        private fun get_key_name_as_string(e: KeyEvent): String
        {
            val key_text = KeyEvent.getKeyText(e.keyCode);
            val modifiers = e.modifiersEx and (KeyEvent.ALT_DOWN_MASK or KeyEvent.CTRL_DOWN_MASK or KeyEvent.SHIFT_DOWN_MASK);
            val key_char = if( modifiers == 0 ) e.keyChar else KeyEvent.CHAR_UNDEFINED;
            return get_key_name_as_string(key_char,key_text);
        }

        private fun get_key_name_as_string(key_char: Char, key_text: String): String
        {
            return if((key_char == KeyEvent.CHAR_UNDEFINED) || (key_text.length > 1))
            {
                key_text;
            }
            else
            {
                String(charArrayOf(key_char));
            }
        }
    }
}