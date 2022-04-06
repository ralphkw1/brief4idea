@file:Suppress("ClassName")

package net.ddns.rkdawenterprises.brief4ijidea

import com.intellij.openapi.actionSystem.KeyboardShortcut
import com.intellij.util.SmartList
import javax.swing.Icon

data class Keymap_action_data
(
    var version: Int = 1,
    var text: String? = null,
    var description: String? = null,
    var action_ID: String? = null,
    var icon: Icon? = null,
    var shortcuts: List<KeyboardShortcut> = SmartList()
)