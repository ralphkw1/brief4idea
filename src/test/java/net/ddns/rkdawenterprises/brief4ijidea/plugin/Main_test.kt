@file:Suppress("ClassName",
               "RedundantSemicolon",
               "LocalVariableName",
               "HardCodedStringLiteral",
               "SpellCheckingInspection")

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
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Column_target
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Remote_robot_client
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.click_on_status_icon_settings
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.click_path
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.close_all_tabs
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.close_tip_of_the_day
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.escape
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_caret_logical_position
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_caret_visual_position
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_current_line_number
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_delete_to_word_boundry_range
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_end_offset
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_line_length
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_line_number
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_lines
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_start_offset
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_visible_area_bottom_offset_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_visible_area_left_offset_of_current_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_visible_area_right_visual_position_of_current_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.get_visible_area_top_offset_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.move_to_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.scroll_to_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.tree_fixtures
import org.assertj.swing.core.MouseButton
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.awt.event.KeyEvent.*
import java.time.Duration

@ExtendWith(Remote_robot_client::class)
class Main_test
{
    @Test
    fun main_test(remote_robot: RemoteRobot) = with(remote_robot)
    {
        // If IDE is open and project/file are set up, then comment this line out to speed iterations.
//        ide_setup()

        idea {
            test_commands()
        }

        println("Finished tests...")
    }

    private fun RemoteRobot.ide_setup()
    {
        project_setup()

        close_tip_of_the_day(this)
        close_all_tabs(this)

        idea {
            file_setup()
        }
    }

    private fun RemoteRobot.project_setup()
    {
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
    }

    private fun IdeaFrame.file_setup()
    {
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
            remoteRobot.actionMenu("New")
                .click()
            remoteRobot.actionMenuItem("Java Class")
                .click()
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
    }

    private fun IdeaFrame.test_commands()
    {
        step("Test the commands") {
            // TODO: Prompt user to close browser.
//            test_help_menu_command()
//            test_quick_java_doc_command()
//            test_undo_redo_commands()
            // TODO: Issuing the hotkey for rename opens the dialog many times. For now, user must close all but one.
//            test_change_output_file_command()
//            test_beginning_of_line_command()
//            test_end_of_line_command()
//            test_top_of_buffer_command()
//            test_end_of_buffer_command()
//            test_top_of_window_command()
//            test_end_of_window_command()
//            test_left_side_of_window_command()
//            test_right_side_of_window_command()
//            test_scroll_buffer_down_in_window_command()
//            test_go_to_line_command()
//            test_delete_line_command()
//            test_delete_next_word_command()
//            test_delete_previous_word_command()
//            test_delete_to_beginning_of_line_command()
//            test_delete_to_end_of_line_command()
//            test_insert_mode_toggle_command()
//            test_open_line_command()
//            test_mark_command()
            test_line_mark_command()
        }
    }

    private fun IdeaFrame.test_line_mark_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Line mark. Description: Starts marking a line at a time.")
        {
            val line = 70;
            val text_of_lines = editor_fixture.get_lines(line, 4);

            editor_fixture.move_to_line(line);

            keyboard {
                hotKey(VK_ALT,
                       VK_L);
                key(VK_DOWN);
                key(VK_DOWN);
                key(VK_DOWN);
            }

            val selected = editor_fixture.selectedText;

            assert(text_of_lines == selected);

            keyboard {
                hotKey(VK_ALT,
                       VK_L);
            }
        }
    }

    private fun IdeaFrame.test_mark_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Mark. Description: Starts normal marking mode.")
        {
            val line = 43;
            val text_of_line = editor_fixture.get_line(line);
            val test_column_start = text_of_line.length / 4;

            editor_fixture.move_to_line(line,
                                        test_column_start);

            keyboard {
                hotKey(VK_ALT,
                       VK_M);
                key(VK_RIGHT);
                key(VK_RIGHT);
                key(VK_RIGHT);
            }

            val test_text = text_of_line.subSequence(test_column_start, test_column_start + 4);
            val selected = editor_fixture.selectedText;

            assert(test_text == selected);

            keyboard {
                hotKey(VK_ALT,
                       VK_M);
            }
        }
    }

    private fun IdeaFrame.test_open_line_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Open line. Description: Inserts a blank line after the current line and places the cursor on the first column of this new line.")
        {
            val line = 43;
            val text_of_line = editor_fixture.get_line(line);
            val text_of_second_line = editor_fixture.get_line(line + 1);
            val text_of_third_line = editor_fixture.get_line(line + 2);
            val test_column = text_of_line.length / 2;
            editor_fixture.move_to_line(line,
                                        test_column)

            assert(text_of_second_line.length > 2)

            keyboard {
                hotKey(VK_CONTROL,
                       VK_ENTER);
            }

            val text_of_line_again = editor_fixture.get_line(line);
            val text_of_second_line_again = editor_fixture.get_line(line + 1);
            val text_of_third_line_again = editor_fixture.get_line(line + 2);

            assert(text_of_line == text_of_line_again);
            assert(text_of_second_line != text_of_second_line_again)
            assert(text_of_second_line == text_of_third_line_again)

            val regex = "^\\s+\$".toRegex();
            assert(regex.matches(text_of_second_line_again))

            keyboard {
                hotKey(VK_ALT,
                       VK_U);
                hotKey(VK_ALT,
                       VK_U);
            }
        }
    }

    private fun IdeaFrame.test_insert_mode_toggle_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Insert mode toggle. Description: Switches between insert mode and overstrike mode.")
        {
            val line = 44;
            var text_of_line = editor_fixture.get_line(line);
            val test_column = text_of_line.length / 2;
            editor_fixture.move_to_line(line,
                                        test_column)

            val test_text = "RalphKW"

            keyboard {
                hotKey(VK_ALT,
                       VK_I);
                enterText(test_text)
            }

            val line_modified = StringBuilder(text_of_line.removeRange(test_column, test_column + test_text.length))
                .insert(test_column, test_text);

            text_of_line = editor_fixture.get_line(line);

            assert(line_modified.toString() == text_of_line);

            keyboard {
                hotKey(VK_ALT,
                       VK_U);
                hotKey(VK_ALT,
                       VK_U);
                hotKey(VK_ALT,
                       VK_I);
            }
        }
    }

    private fun IdeaFrame.test_delete_to_end_of_line_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Delete to end of line. Description: Deletes all characters from the current position to the end of the line.")
        {
            val line = 44;
            var text_of_line = editor_fixture.get_line(line);
            val test_column = text_of_line.length / 2;
            editor_fixture.move_to_line(line, test_column)

            keyboard {
                hotKey(VK_ALT,
                       VK_K);
            }

            val line_modified = text_of_line.removeRange(test_column, (text_of_line.length - 1));
            text_of_line = editor_fixture.get_line(line);

            assert(line_modified == text_of_line);

            keyboard {
                hotKey(VK_ALT,
                       VK_U);
                hotKey(VK_ALT,
                       VK_U);
            }
        }
    }

    private fun IdeaFrame.test_delete_to_beginning_of_line_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Delete to beginning of line. Description: Deletes all characters before the cursor to the beginning of the line.")
        {
            val line = 44;
            var text_of_line = editor_fixture.get_line(line);
            val test_column = text_of_line.length / 2;
            editor_fixture.move_to_line(line, test_column)

            keyboard {
                hotKey(VK_CONTROL,
                       VK_K);
            }

            val line_modified = text_of_line.removeRange(0, test_column);
            text_of_line = editor_fixture.get_line(line);

            assert(line_modified == text_of_line);
            
            keyboard {
                hotKey(VK_ALT,
                       VK_U);
                hotKey(VK_ALT,
                       VK_U);
            }
        }
    }

    private fun IdeaFrame.test_delete_previous_word_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Delete previous word. Description: Deletes from the cursor position to the beginning of the previous word.")
        {
            val line = 44;
            var text_of_line = editor_fixture.get_line(line);
            editor_fixture.clickOnOffset(editor_fixture.get_end_offset(line));
            Thread.sleep(500);

            val range_to_delete = editor_fixture.get_delete_to_word_boundry_range(false);
            val line_modified = text_of_line.removeRange(range_to_delete[0].column, range_to_delete[1].column);

            keyboard {
                hotKey(VK_CONTROL,
                       VK_BACK_SPACE);
            }

            text_of_line = editor_fixture.get_line(line);
            assert(line_modified == text_of_line);

            keyboard {
                hotKey(VK_ALT,
                       VK_U);
                hotKey(VK_ALT,
                       VK_U);
            }
        }
    }

    private fun IdeaFrame.test_delete_next_word_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Delete next word. Description: Deletes from the cursor position to the start of the next word.")
        {
            val line = 306;
            var text_of_line = editor_fixture.get_line(line);
            editor_fixture.clickOnOffset(editor_fixture.get_start_offset(line));
            Thread.sleep(500)

            val range_to_delete = editor_fixture.get_delete_to_word_boundry_range(true);
            val line_modified = text_of_line.removeRange(range_to_delete[0].column, range_to_delete[1].column);

            keyboard {
                hotKey(VK_ALT,
                       VK_BACK_SPACE)
            }

            text_of_line = editor_fixture.get_line(line);
            assert(line_modified == text_of_line);

            keyboard {
                hotKey(VK_ALT,
                       VK_U);
                hotKey(VK_ALT,
                       VK_U);
            }
        }
    }

    private fun IdeaFrame.test_delete_line_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Delete line. Description: Deletes the entire current line.")
        {
            val line = 306;
            editor_fixture.clickOnOffset(editor_fixture.get_start_offset(line));
            Thread.sleep(500)

            val text_of_line_previous = editor_fixture.get_line(line - 1);
            var text_of_line = editor_fixture.get_line(line);
            val text_of_line_next = editor_fixture.get_line(line + 1);

            assert((text_of_line != text_of_line_previous) && (text_of_line != text_of_line_next))

            editor_fixture.clickOnOffset(editor_fixture.get_start_offset(line));
            Thread.sleep(500)

            keyboard {
                hotKey(VK_ALT,
                       VK_D)
            }

            text_of_line = editor_fixture.get_line(line);

            assert((text_of_line != text_of_line_previous) && (text_of_line == text_of_line_next))

            keyboard {
                hotKey(VK_ALT,
                       VK_U)
                hotKey(VK_ALT,
                       VK_U)
            }
        }
    }

    private fun IdeaFrame.test_go_to_line_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Go to line. Description: Moves the cursor to the specified line number.")
        {
            val line = 178;
            editor_fixture.clickOnOffset(editor_fixture.get_start_offset(line));
            Thread.sleep(500)

            val current_line = editor_fixture.get_caret_logical_position().line;
            assert(current_line == line);

            keyboard {
                hotKey(VK_ALT,
                       VK_G)
            }

            dialog("Go to Line:Column")
            {
                button("OK").click()
            }
        }
    }

    private fun IdeaFrame.test_scroll_buffer_up_in_window_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Scroll buffer up in window. Description: Moves the buffer, if possible, up one line in the window, keeping the cursor on the same line.")
        {
            val line = 178;
            editor_fixture.clickOnOffset(editor_fixture.get_start_offset(line));
            Thread.sleep(500)

            val starting_offset = editor_fixture.caretOffset
            val starting_window_end_line = editor_fixture.get_visible_area_bottom_offset_line()[1]
            val starting_window_beginning_line = editor_fixture.get_visible_area_top_offset_line()[1]

            keyboard {
                hotKey(VK_CONTROL,
                       VK_E)
            }

            val final_offset = editor_fixture.caretOffset
            val final_window_end_line = editor_fixture.get_visible_area_bottom_offset_line()[1]
            val final_window_beginning_line = editor_fixture.get_visible_area_top_offset_line()[1]

            assert(starting_offset == final_offset)
            assert((starting_window_end_line - 1) == final_window_end_line)
            assert((starting_window_beginning_line - 1) == final_window_beginning_line)
        }
    }

    private fun IdeaFrame.test_scroll_buffer_down_in_window_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Scroll buffer down in window. Description: Moves the buffer, if possible, down one line in the window, keeping the cursor on the same line.")
        {
            val line = 178;
            editor_fixture.clickOnOffset(editor_fixture.get_start_offset(line));
            Thread.sleep(500)

            val starting_offset = editor_fixture.caretOffset
            val starting_window_end_line = editor_fixture.get_visible_area_bottom_offset_line()[1]
            val starting_window_beginning_line = editor_fixture.get_visible_area_top_offset_line()[1]

            keyboard {
                hotKey(VK_CONTROL,
                       VK_D)
            }

            val final_offset = editor_fixture.caretOffset
            val final_window_end_line = editor_fixture.get_visible_area_bottom_offset_line()[1]
            val final_window_beginning_line = editor_fixture.get_visible_area_top_offset_line()[1]

            assert(starting_offset == final_offset)
            assert((starting_window_end_line + 1) == final_window_end_line)
            assert((starting_window_beginning_line + 1) == final_window_beginning_line)
        }
    }

    private fun IdeaFrame.test_right_side_of_window_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Right side of window. Description: Moves the cursor to the right side of the window, regardless of the length of the line.")
        {
            val line = 144;
            editor_fixture.clickOnOffset(editor_fixture.get_start_offset(line));
            Thread.sleep(500)

            val start_visual_position = editor_fixture.get_caret_visual_position();
            val end_visual_position = editor_fixture.get_visible_area_right_visual_position_of_current_line();
            assert( start_visual_position.column != end_visual_position.column )

            keyboard {
                hotKey(VK_SHIFT,
                       VK_END)
            }

            dialog("Change Settings for this Command") {
                findText("Don't ask again").click()
                button("OK").click()
            }

            click_on_status_icon_settings();
            dialog("Settings") {
                checkBox("Do not show virtual space setting dialog again.").unselect()
                tree_fixtures[0].click_path("Editor, General")
                checkBox("After the end of line").select()
                button("OK").click()
            }

            keyboard {
                hotKey(VK_SHIFT,
                       VK_END)
            }

            val current_visual_position = editor_fixture.get_caret_visual_position();
            assert( current_visual_position.column == end_visual_position.column )

            click_on_status_icon_settings();
            dialog("Settings") {
                tree_fixtures[0].click_path("Editor, General")
                checkBox("After the end of line").unselect()
                button("OK").click()
            }
        }
    }

    private fun IdeaFrame.test_left_side_of_window_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Left side of window. Description: Moves the cursor to the left side of the window.")
        {
            val line = 148;
            editor_fixture.clickOnOffset(editor_fixture.get_end_offset(line));
            Thread.sleep(500)
            val left_side_of_window_offset = editor_fixture.get_visible_area_left_offset_of_current_line();

            keyboard {
                hotKey(VK_SHIFT,
                       VK_HOME)
            }

            assert(editor_fixture.caretOffset == left_side_of_window_offset);
        }
    }

    private fun IdeaFrame.test_end_of_window_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: End of window. Description: Moves the cursor to the last line of the current window, retaining the column position.")
        {
            val column = 14
            editor_fixture.move_to_line(18,
                                                    column)
            val starting_offset = editor_fixture.caretOffset

            val window_end_offset_line = editor_fixture.get_visible_area_bottom_offset_line()
            val window_end_offset = window_end_offset_line[0]
            val window_end_line = window_end_offset_line[1]
            val window_end_offset_alternate = window_end_offset_line[2]
            val window_end_line_alternate = window_end_offset_line[3]

            assert( (starting_offset != window_end_offset) && (starting_offset != window_end_offset_alternate))

            val window_end_line_length = editor_fixture.get_line_length(window_end_line)
            val window_end_line_length_alternate = editor_fixture.get_line_length(window_end_line_alternate)
            val window_end_target_offset = window_end_offset - (window_end_line_length - column)
            val window_end_target_offset_alternate = window_end_offset_alternate - (window_end_line_length_alternate - column)

            keyboard {
                hotKey(VK_CONTROL,
                       VK_END)
            }
            val current_offset = editor_fixture.caretOffset
            val current_line = editor_fixture.get_current_line_number()

            assert(((current_offset == window_end_target_offset) &&
                           (current_line == window_end_line)) ||
                           ((current_offset == window_end_target_offset_alternate) &&
                                   (current_line == window_end_line_alternate)))
        }
    }

    private fun IdeaFrame.test_top_of_window_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Top of window. Description: Moves the cursor to the top line of the current window, retaining the column position.")
        {
            val column = 31
            editor_fixture.move_to_line(34, column)

            val starting_offset = editor_fixture.caretOffset

            val window_beginning_offset_line = editor_fixture.get_visible_area_top_offset_line()
            val window_beginning_offset = window_beginning_offset_line[0]
            val window_beginning_line = window_beginning_offset_line[1]
            val window_beginning_offset_alternate = window_beginning_offset_line[2]
            val window_beginning_line_alternate = window_beginning_offset_line[3]
            assert((starting_offset != window_beginning_offset) &&
                           (starting_offset != window_beginning_offset_alternate))

            keyboard {
                hotKey(VK_CONTROL,
                       VK_HOME)
            }
            val current_offset = editor_fixture.caretOffset
            val current_line = editor_fixture.get_current_line_number()
            assert(((current_offset == (window_beginning_offset + column)) &&
                    (current_line == window_beginning_line)) ||
                           ((current_offset == (window_beginning_offset_alternate + column)) &&
                                   (current_line == window_beginning_line_alternate)))
        }
    }

    private fun IdeaFrame.test_end_of_buffer_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: End of buffer. Description: Moves the cursor to the last character in the buffer.")
        {
            editor_fixture.move_to_line(159,
                                                    68)
            val starting_offset = editor_fixture.caretOffset
            val end_offset = editor_fixture.get_end_offset()
            val end_line = editor_fixture.get_line_number(end_offset)
            assert(starting_offset != end_offset)

            keyboard {
                hotKey(VK_CONTROL,
                       VK_PAGE_DOWN)
            }
            val current_offset = editor_fixture.caretOffset
            val current_line = editor_fixture.get_current_line_number()
            assert((current_offset == end_offset) && (current_line == end_line))
        }
    }

    private fun IdeaFrame.test_top_of_buffer_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Top of buffer. Description: Moves the cursor to the first character of the buffer.")
        {
            editor_fixture.move_to_line(159, 68)
            val starting_offset = editor_fixture.caretOffset
            assert(starting_offset != 0)

            keyboard {
                hotKey(VK_CONTROL,
                       VK_PAGE_UP)
            }
            val current_offset = editor_fixture.caretOffset
            val current_line = editor_fixture.get_current_line_number()
            assert((current_offset == 0) && (current_line == 0))
        }
    }

    private fun IdeaFrame.test_end_of_line_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: End of line. Description: Places the cursor at the last valid character of the current line, window, or file.")
        {
            val line = 159
            val column = 68
            editor_fixture.move_to_line(line, Column_target.END)
            val end_of_line_offset = editor_fixture.caretOffset
            editor_fixture.move_to_line(line, column)
            val window_end_offset_line = editor_fixture.get_visible_area_bottom_offset_line()
            val window_end_offset = window_end_offset_line[0]
            val window_end_line = window_end_offset_line[1]
            val window_end_offset_alternate = window_end_offset_line[2]
            val window_end_line_alternate = window_end_offset_line[3]

            val starting_offset = editor_fixture.caretOffset
            assert(starting_offset != end_of_line_offset)

            keyboard { key(VK_END) }
            var current_offset = editor_fixture.caretOffset
            var current_line = editor_fixture.get_current_line_number()
            assert((current_offset == end_of_line_offset) && (current_line == line))

            keyboard { key(VK_END) }
            current_offset = editor_fixture.caretOffset
            current_line = editor_fixture.get_current_line_number()
            assert(((current_offset == window_end_offset) && (current_line == window_end_line)) ||
                           (current_offset == window_end_offset_alternate) && ((current_line == window_end_line_alternate)))

            keyboard { key(VK_END) }
            current_offset = editor_fixture.caretOffset
            current_line = editor_fixture.get_current_line_number()
            val end_offset = editor_fixture.get_end_offset()
            val end_line = editor_fixture.get_line_number(end_offset)
            assert((current_line == end_line) && (current_offset == end_offset))
        }
    }

    private fun IdeaFrame.test_beginning_of_line_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Beginning of line. Description: Places the cursor at column 1 of the current line, window, or file.")
        {
            val line = 159
            val column = 68
            editor_fixture.move_to_line(line)
            val beginning_of_line_offset = editor_fixture.caretOffset
            editor_fixture.move_to_line(line, column)
            val window_beginning_offset_line = editor_fixture.get_visible_area_top_offset_line();
            val window_beginning_offset = window_beginning_offset_line[0]
            val window_beginning_line = window_beginning_offset_line[1]
            val window_beginning_offset_alternate = window_beginning_offset_line[2]
            val window_beginning_line_alternate = window_beginning_offset_line[3]

            val starting_offset = editor_fixture.caretOffset
            assert(starting_offset != beginning_of_line_offset)

            keyboard { key(VK_HOME) }
            var current_offset = editor_fixture.caretOffset
            var current_line = editor_fixture.get_current_line_number()
            assert((current_offset == beginning_of_line_offset) && (current_line == line))

            keyboard { key(VK_HOME) }
            current_offset = editor_fixture.caretOffset
            current_line = editor_fixture.get_current_line_number()
            assert(((current_line == window_beginning_line) && (current_offset == window_beginning_offset)) ||
                           ((current_line == window_beginning_line_alternate) && (current_offset == window_beginning_offset_alternate)))

            keyboard { key(VK_HOME) }
            current_offset = editor_fixture.caretOffset
            current_line = editor_fixture.get_current_line_number()
            assert((current_line == 0) && (current_offset == 0))
        }
    }

    private fun IdeaFrame.test_change_output_file_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Change output file. Description: Changes the output file name for the current buffer.")
        {
            editor_fixture.keyboard {
                hotKey(VK_ALT,
                       VK_O)
            }

            dialog("Rename",
                   Duration.ofSeconds(60)) {
                button("Cancel").click()
            }
        }
    }

    private fun IdeaFrame.test_undo_redo_commands()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor
        val line = 49
        var string_at_line_modified = ""

        step("Command: Undo. Description: Reverses the effect of any typing or commands that modified an open file.")
        {
            keyboard { key(VK_ESCAPE) }

            val string_at_line = editor_fixture.get_line(line)

            val index = 14
            val offset = editor_fixture.caretOffset
            editor_fixture.clickOnOffset(offset + index)
            waitFor { editor_fixture.caretOffset == (offset + index) }
            keyboard { key(VK_DELETE) }
            keyboard { key(VK_DELETE) }
            string_at_line_modified = editor_fixture.get_line(line)
            assert(string_at_line_modified == string_at_line.removeRange(index,
                                                                         index + 2))

            editor_fixture.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            editor_fixture.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            val string_at_line_modified_undo = editor_fixture.get_line(line)
            assert(string_at_line == string_at_line_modified_undo)
        }

        step("Command: Redo. Description: Reverses the effect of commands that have been undone.")
        {
            editor_fixture.keyboard {
                hotKey(VK_CONTROL,
                       VK_U)
            }

            editor_fixture.keyboard {
                hotKey(VK_CONTROL,
                       VK_U)
            }

            val string_at_line_modified_redo = editor_fixture.get_line(line)
            assert(string_at_line_modified_redo == string_at_line_modified)

            editor_fixture.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            editor_fixture.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }
        }
    }

    private fun IdeaFrame.test_quick_java_doc_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Quick Java Doc. Description: Show contextual documentation popup.")
        {
            editor_fixture.scroll_to_line(42);
            waitFor { editor_fixture.hasText("SuppressWarnings") }
            editor_fixture.findText("SuppressWarnings")
                .click()
            editor_fixture.keyboard {
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

    private fun IdeaFrame.test_help_menu_command()
    {
        val text_editor_fixture = textEditor()
        val editor_fixture = text_editor_fixture.editor

        step("Command: Help. Description: Open general help menu.")
        {
            editor_fixture.keyboard {
                hotKey(VK_ALT,
                       VK_H)
            }

            Thread.sleep(5000)

            waitFor(Duration.ofSeconds(60))
            {
                editor_fixture.hasFocus
            }
        }
    }

// TODO:
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
