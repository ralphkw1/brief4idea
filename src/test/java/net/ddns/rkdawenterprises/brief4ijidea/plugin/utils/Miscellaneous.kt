// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package net.ddns.rkdawenterprises.brief4ijidea.plugin.utils

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.fixtures.EditorFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.Keyboard
import com.intellij.remoterobot.utils.waitFor
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.DialogFixture
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.DialogFixture.Companion.byTitle
import net.ddns.rkdawenterprises.brief4ijidea.plugin.pages.IdeaFrame
import java.awt.Point
import java.time.Duration

class Miscellaneous(private val remoteRobot: RemoteRobot)
{
    private val keyboard: Keyboard = Keyboard(remoteRobot)

    fun close_tip_of_the_day() = optional_step("Close Tip of the Day if it appears")
    {
        waitFor(Duration.ofSeconds(30)) {
            remoteRobot.findAll(DialogFixture::class.java,
                                byXpath("//div[@class='MyDialog'][.//div[@text='Running startup activities...']]"))
                .isEmpty()
        }

        val idea: IdeaFrame = remoteRobot.find(IdeaFrame::class.java)
        idea.dumbAware {
                     idea.find(DialogFixture::class.java,
                               byTitle("Tip of the Day")).button("Close").click()
        }

    }

    fun close_all_tabs() = step("Close all existing tabs")
    {
        remoteRobot.findAll<CommonContainerFixture>(byXpath("//div[@class='EditorTabs']//div[@class='SingleHeightLabel']")).forEach {
            it.find<ComponentFixture>(byXpath("//div[@class='InplaceButton']")).click()
        }
    }

    private fun optional_step(step_name: String, code: () -> Unit) = step(step_name)
    {
        try
        {
            code()
        }
        catch(ignore: Throwable)
        {
            println("$step_name failure ignored...")
        }
    }

    companion object
    {
        @JvmStatic
        fun String.escape(): String = this.replace("\n", "\\n")

        @JvmStatic
        fun EditorFixture.scroll_to_line(line_number: Int)
        {
            val script =
                """
                    const editor = local.get('editor')
                    const document = local.get('document')
                    const offset = document.getLineStartOffset(${line_number} - 1)
                    editor.getScrollingModel().scrollTo(editor.offsetToLogicalPosition(offset), com.intellij.openapi.editor.ScrollType.CENTER)
                """.trimIndent();
            runJs(script, true)
            Thread.sleep(500)
        }

        @JvmStatic
        fun EditorFixture.move_to_line(line_number: Int)
        {
            val script =
                """
                    importClass(com.intellij.openapi.editor.ScrollType)
                    const editor = component.getEditor()
                    const document = editor.getDocument()
                    const offset = document.getLineStartOffset($line_number - 1)
                    editor.getScrollingModel().scrollTo(editor.offsetToLogicalPosition(offset), ScrollType.CENTER)
                    const visual_position = editor.offsetToVisualPosition(offset)
                    editor.visualPositionToXY(visual_position)
                """.trimIndent()
            val point = callJs<Point>(script, true)
            Thread.sleep(500)
            click(point)
        }

        @JvmStatic
        fun EditorFixture.get_line(line_number: Int): String
        {
            move_to_line(line_number)

            val script =
                """
                    importPackage(com.intellij.openapi.command)

                    const editor = local.get('editor')
                    const project = editor.getProject()
            
                    WriteCommandAction.runWriteCommandAction(project, new Runnable({
                        run: function () {
                            editor.getSelectionModel().selectLineAtCaret();
                        }
                    }))
                """.trimIndent();

            runJs(script, true)
            Thread.sleep(500)

            return selectedText
        }
    }
}