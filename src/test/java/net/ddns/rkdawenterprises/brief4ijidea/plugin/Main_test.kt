@file:Suppress("ClassName",
               "RedundantSemicolon",
               "LocalVariableName")

package net.ddns.rkdawenterprises.brief4ijidea.plugin

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.keyboard
import com.intellij.remoterobot.utils.waitFor
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.IdeaFrame
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.actionMenu
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.actionMenuItem
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.dialog
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.idea
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.welcomeFrame
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.escape
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.get_current_line_number
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.get_end_offset
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.get_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.get_line_number
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.get_visible_area_bottom_offset_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.get_visible_area_top_offset_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.move_to_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.scroll_to_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Remote_robot_client
import org.assertj.swing.core.MouseButton
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_A
import java.awt.event.KeyEvent.VK_ALT
import java.awt.event.KeyEvent.VK_CONTROL
import java.awt.event.KeyEvent.VK_DELETE
import java.awt.event.KeyEvent.VK_END
import java.awt.event.KeyEvent.VK_ESCAPE
import java.awt.event.KeyEvent.VK_H
import java.awt.event.KeyEvent.VK_O
import java.awt.event.KeyEvent.VK_SPACE
import java.awt.event.KeyEvent.VK_U
import java.time.Duration


@ExtendWith(Remote_robot_client::class)
class Main_test
{
    @Test
    fun main_test(remote_robot: RemoteRobot) = with(remote_robot) {
        val shared_steps = Miscellaneous(this)

        welcomeFrame {
            create_new_project_link.click()

            dialog("New Project") {
                findText("Java").click()

                find(ComponentFixture::class.java,
                     byXpath("//div[@class='FrameworksTree']")).findText("Kotlin/JVM")
                    .click()

                runJs("robot.pressAndReleaseKey($VK_SPACE)")

                button("Next").click()
                button("Finish").click()
            }
        }

        with(shared_steps) {
            close_tip_of_the_day()
            close_all_tabs()
        }

        idea {
            waitFor(Duration.ofMinutes(5)) { isDumbMode().not() }

            step("Create the test java file") {
                with(projectViewTree) {
                    if(hasText("src").not())
                    {
                        findText(projectName).doubleClick()
                        waitFor { hasText("src") }
                    }
                    findText("src").click(MouseButton.RIGHT_BUTTON)
                }
                actionMenu("New").click()
                actionMenuItem("Java Class").click()
                keyboard { enterText("Test"); enter() }
            }

            with(textEditor()) {
                step("Populate the test java file") {
                    waitFor { editor.hasText("Test") }
                    editor.findText("Test")
                        .click()
                    keyboard {
                        key(VK_END)
                        enter()
                    }
                    keyboard {
                        hotKey(VK_CONTROL,
                               VK_A)
                    }
                    keyboard { key(VK_DELETE) }
                    editor.insertTextAtLine(0,
                                            0,
                                            Test_data.java_example.trimIndent()
                                                .escape())
                }
            }

            step("Test the commands") {
                test_commands()
            }

            println("Finished tests...")

        }
    }

    private fun IdeaFrame.test_commands()
    {
//        test_help_menu_command()
//        test_quick_java_doc_command()
//        test_undo_redo_commands()
//        test_change_output_file_command()
//        test_beginning_of_line_command()
//        test_end_of_line_command()
        test_top_of_buffer_command()
    }

    private fun IdeaFrame.test_top_of_buffer_command()
    {
        val text_editor_fixture = textEditor()

        step("Command: Top of buffer. Description: Moves the cursor to the first character of the buffer.")
        {

        }
    }

    private fun IdeaFrame.test_end_of_line_command()
    {
        val text_editor_fixture = textEditor()

        step("Command: End of line. Description: Places the cursor at the last valid character of the current line, window, or file.")
        {
            val line = 159
            val column = 68
            text_editor_fixture.editor.move_to_line(line,
                                                    Miscellaneous.Companion.Column_target.END)
            val end_of_line_offset = text_editor_fixture.editor.caretOffset
            text_editor_fixture.editor.move_to_line(line,
                                                    column)
            val window_end_offset_line = text_editor_fixture.editor.get_visible_area_bottom_offset_line()
                .split(",")
                .toTypedArray()
            val window_end_offset = window_end_offset_line[0].toInt()
            val window_end_line = window_end_offset_line[1].toInt()
            val window_end_offset_alternate = window_end_offset_line[2].toInt()
            val window_end_line_alternate = window_end_offset_line[3].toInt()

            val starting_offset = text_editor_fixture.editor.caretOffset
            assert(starting_offset != end_of_line_offset)

            keyboard { key(VK_END) }
            var current_offset = text_editor_fixture.editor.caretOffset
            var current_line = text_editor_fixture.editor.get_current_line_number()
            assert((current_offset == end_of_line_offset) && (current_line == line))

            keyboard { key(VK_END) }
            current_offset = text_editor_fixture.editor.caretOffset
            current_line = text_editor_fixture.editor.get_current_line_number()
            assert(((current_offset == window_end_offset) && (current_line == window_end_line)) ||
                           (current_offset == window_end_offset_alternate) && ((current_line == window_end_line_alternate)))

            keyboard { key(VK_END) }
            current_offset = text_editor_fixture.editor.caretOffset
            current_line = text_editor_fixture.editor.get_current_line_number()
            val end_offset = text_editor_fixture.editor.get_end_offset()
            val end_line = text_editor_fixture.editor.get_line_number(end_offset)
            assert((current_line == end_line) && (current_offset == end_offset))
        }
    }

    private fun IdeaFrame.test_beginning_of_line_command()
    {
        val text_editor_fixture = textEditor()

        step("Command: Beginning of line. Description: Places the cursor at column 1 of the current line, window, or file.")
        {
            val line = 159
            val column = 68
            text_editor_fixture.editor.move_to_line(line)
            val beginning_of_line_offset = text_editor_fixture.editor.caretOffset
            text_editor_fixture.editor.move_to_line(line,
                                                    column)

            val window_beginning_offset_line = text_editor_fixture.editor.get_visible_area_top_offset_line()
                .split(",")
                .toTypedArray()
            val window_beginning_offset = window_beginning_offset_line[0].toInt()
            val window_beginning_line = window_beginning_offset_line[1].toInt()
            val window_beginning_offset_alternate = window_beginning_offset_line[2].toInt()
            val window_beginning_line_alternate = window_beginning_offset_line[3].toInt()

            val starting_offset = text_editor_fixture.editor.caretOffset
            assert(starting_offset != beginning_of_line_offset)

            keyboard { key(KeyEvent.VK_HOME) }
            var current_offset = text_editor_fixture.editor.caretOffset
            var current_line = text_editor_fixture.editor.get_current_line_number()
            assert((current_offset == beginning_of_line_offset) && (current_line == line))

            keyboard { key(KeyEvent.VK_HOME) }
            current_offset = text_editor_fixture.editor.caretOffset
            current_line = text_editor_fixture.editor.get_current_line_number()
            assert(((current_line == window_beginning_line) && (current_offset == window_beginning_offset)) ||
                           ((current_line == window_beginning_line_alternate) && (current_offset == window_beginning_offset_alternate)))

            keyboard { key(KeyEvent.VK_HOME) }
            current_offset = text_editor_fixture.editor.caretOffset
            current_line = text_editor_fixture.editor.get_current_line_number()
            assert((current_line == 0) && (current_offset == 0))
        }
    }

    private fun IdeaFrame.test_help_menu_command()
    {
        val text_editor_fixture = textEditor()

        step("Command: Help. Description: Open general help menu.")
        {
            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_H)
            }

            Thread.sleep(5000)

            waitFor(Duration.ofSeconds(60))
            {
                text_editor_fixture.editor.hasFocus
            }
        }
    }

    private fun IdeaFrame.test_quick_java_doc_command()
    {
        val text_editor_fixture = textEditor()

        step("Command: Quick Java Doc. Description: Show contextual documentation popup.")
        {
            text_editor_fixture.editor.scroll_to_line(42);
            waitFor { text_editor_fixture.editor.hasText("SuppressWarnings") }
            text_editor_fixture.editor.findText("SuppressWarnings")
                .click()
            text_editor_fixture.editor.keyboard {
                hotKey(VK_CONTROL,
                       VK_H)
            }

            waitFor { heavyWeightWindows().size == 1 }
            val all_text = heavyWeightWindows()[0].find(CommonContainerFixture::class.java,
                                                        byXpath("//div[@class='JEditorPane']"),
                                                        Duration.ofSeconds(5))
                .findAllText()
            var found_it = false
            for(i in all_text.indices)
            {
                if(all_text[i].text.contains("public interface"))
                {
                    if(all_text[i + 1].text.contains("SuppressWarnings")) found_it = true
                }
            }

            assert(found_it)
        }
    }

    private fun IdeaFrame.test_undo_redo_commands()
    {
        val text_editor_fixture = textEditor()
        val line = 49
        var string_at_line_modified: String = ""

        step("Command: Undo. Description: Reverses the effect of any typing or commands that modified an open file.")
        {
            keyboard { key(VK_ESCAPE) }

            val string_at_line = text_editor_fixture.editor.get_line(line)

            val index = 14
            val offset = text_editor_fixture.editor.caretOffset
            text_editor_fixture.editor.clickOnOffset(offset + index)
            waitFor { text_editor_fixture.editor.caretOffset == (offset + index) }
            keyboard { key(VK_DELETE) }
            keyboard { key(VK_DELETE) }
            string_at_line_modified = text_editor_fixture.editor.get_line(line)
            assert(string_at_line_modified == string_at_line.removeRange(index,
                                                                         index + 2))

            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            val string_at_line_modified_undo = text_editor_fixture.editor.get_line(line)
            assert(string_at_line == string_at_line_modified_undo)
        }

        step("Command: Redo. Description: Reverses the effect of commands that have been undone.")
        {
            text_editor_fixture.editor.keyboard {
                hotKey(VK_CONTROL,
                       VK_U)
            }

            text_editor_fixture.editor.keyboard {
                hotKey(VK_CONTROL,
                       VK_U)
            }

            val string_at_line_modified_redo = text_editor_fixture.editor.get_line(line)
            assert(string_at_line_modified_redo == string_at_line_modified)

            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }
        }
    }

    private fun IdeaFrame.test_change_output_file_command()
    {
        val text_editor_fixture = textEditor()

        step("Command: Change output file. Description: Changes the output file name for the current buffer.")
        {
            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_O)
            }

            dialog("Rename",
                   Duration.ofSeconds(60)) {
                button("Cancel").click()
            }
        }
    }

//    @AfterEach
//    fun closeProject(remoteRobot: RemoteRobot) = with(remoteRobot) {
//        idea {
//            if(remoteRobot.isMac())
//            {
//                keyboard {
//                    hotKey(VK_SHIFT,
//                           VK_META,
//                           VK_A);
//                    enterText("Close Project");
//                    enter();
//                }
//            }
//            else
//            {
//                menuBar.select("File",
//                               "Close Project");
//            }
//        }
//    }
}
