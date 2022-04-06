@file:Suppress("ClassName",
               "RedundantSemicolon",
               "LocalVariableName")

package net.ddns.rkdawenterprises.brief4ijidea.plugin

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.fixtures.TextEditorFixture
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
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.get_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Miscellaneous.Companion.scroll_to_line
import net.ddns.rkdawenterprises.brief4ijidea.plugin.utils.Remote_robot_client
import org.assertj.swing.core.MouseButton
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.awt.event.KeyEvent.VK_A
import java.awt.event.KeyEvent.VK_ALT
import java.awt.event.KeyEvent.VK_CONTROL
import java.awt.event.KeyEvent.VK_DELETE
import java.awt.event.KeyEvent.VK_END
import java.awt.event.KeyEvent.VK_ESCAPE
import java.awt.event.KeyEvent.VK_H
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
//                test_help_menu_command(textEditor())
//                test_quick_java_doc_command(this)
//                test_undo_redo_commands(textEditor())
                test_change_output_file_command(textEditor())

            }

            println("Finished tests...")

        }
    }

    private fun IdeaFrame.test_help_menu_command(text_editor_fixture: TextEditorFixture)
    {
        step("Command: Help. Description: Open general help menu.")
        {
            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_H)
            }

            Thread.sleep(5000)

            waitFor(Duration.ofSeconds(30))
            {
                text_editor_fixture.editor.hasFocus
            }
        }
    }

    private fun IdeaFrame.test_quick_java_doc_command(idea_frame: IdeaFrame)
    {
        step("Command: Quick Java Doc. Description: Show contextual documentation popup.")
        {
            val text_editor_fixture: TextEditorFixture = idea_frame.textEditor()
            text_editor_fixture.editor.scroll_to_line(41);
            waitFor { text_editor_fixture.editor.hasText("SuppressWarnings") }
            text_editor_fixture.editor.findText("SuppressWarnings")
                .click()
            text_editor_fixture.editor.keyboard {
                hotKey(VK_CONTROL,
                       VK_H)
            }

            waitFor { idea_frame.heavyWeightWindows().size == 1 }
            val all_text = idea_frame.heavyWeightWindows()[0].find(CommonContainerFixture::class.java,
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

    private fun IdeaFrame.test_undo_redo_commands(text_editor_fixture: TextEditorFixture)
    {
        var string_at_48_modified: String = ""

        step("Command: Undo. Description: Reverses the effect of any typing or commands that modified an open file.")
        {
            keyboard { key(VK_ESCAPE) }

            val string_at_48 = text_editor_fixture.editor.get_line(48)

            val index = 14
            val offset = text_editor_fixture.editor.caretOffset
            text_editor_fixture.editor.clickOnOffset(offset + index)
            waitFor { text_editor_fixture.editor.caretOffset == (offset + index) }
            keyboard { key(VK_DELETE) }
            keyboard { key(VK_DELETE) }
            string_at_48_modified = text_editor_fixture.editor.get_line(48)
            assert(string_at_48_modified == string_at_48.removeRange(index,
                                                                     index + 2))

            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_U)
            }

            val string_at_48_modified_undo = text_editor_fixture.editor.get_line(48)
            assert(string_at_48 == string_at_48_modified_undo)
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

            val string_at_48_modified_redo = text_editor_fixture.editor.get_line(48)
            assert(string_at_48_modified_redo == string_at_48_modified)

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

    private fun IdeaFrame.test_change_output_file_command(text_editor_fixture: TextEditorFixture)
    {
        step("Command: Change output file. Description: Changes the output file name for the current buffer.")
        {
            text_editor_fixture.editor.keyboard {
                hotKey(VK_ALT,
                       VK_H)
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
