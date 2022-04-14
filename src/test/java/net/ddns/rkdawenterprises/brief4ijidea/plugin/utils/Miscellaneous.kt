// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:Suppress("ClassName")

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
                    const offset = document.getLineStartOffset($line_number)
                    editor.getScrollingModel().scrollTo(editor.offsetToLogicalPosition(offset), com.intellij.openapi.editor.ScrollType.CENTER)
                """.trimIndent();
            runJs(script, true)
            Thread.sleep(500)
        }

        enum class Column_target { START, END }

        @JvmStatic
        fun EditorFixture.move_to_line(line_number: Int, column_target: Column_target = Column_target.START)
        {
            if(column_target == Column_target.START)
            {
                move_to_line(line_number,
                             0)
            }
            else if(column_target == Column_target.END)
            {
                move_to_line(line_number,
                             get_end_column(line_number))
            }
        }

        @JvmStatic
        fun EditorFixture.move_to_line(line_number: Int, column_number: Int)
        {
            val script =
                """
                    importClass(com.intellij.openapi.editor.ScrollType)
                    const editor = component.getEditor()
                    const document = editor.getDocument()
                    const offset = document.getLineStartOffset($line_number) + $column_number
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

        /**
         * Obtains the caret offset and line number of the beginning of the first line in the current visible area.
         * Also returns an alternate (the next line) offset/line since scrolling may not allow the caret
         * to be on the first visible line.
         *
         * @return CSV string with "offset,line,offset,line".
         */
        @JvmStatic
        fun EditorFixture.get_visible_area_top_offset_line(): String
        {
            val script =
                """
                    importPackage(com.intellij.util)
                    
                    const editor = local.get('editor')
                    const document = local.get('document')
                    
                    const is_true_smooth_scrolling_enabled = SystemProperties.isTrueSmoothScrollingEnabled()
                    const model = editor.scrollingModel
                    const visible_area = is_true_smooth_scrolling_enabled ? model.getVisibleAreaOnScrollingFinished() : model.getVisibleArea()
                    const visible_area_top_line_number = editor.yToVisualLine( visible_area.y )
                    if( ( visible_area.y > editor.visualLineToY( visible_area_top_line_number ) ) &&
                            ( ( visible_area.y + visible_area.height ) > editor.visualLineToY( visible_area_top_line_number + 1 ) ) )
                    {
                        visible_area_top_line_number++;
                    }
                    
                    const offset = document.getLineStartOffset(visible_area_top_line_number)
                    const alternate_offset = document.getLineStartOffset(visible_area_top_line_number + 1)
                    
                    offset.toString() + ',' + visible_area_top_line_number.toString() + ',' +
                    alternate_offset.toString() + ',' + (visible_area_top_line_number + 1).toString()
                """.trimIndent();
            return callJs(script, true)
        }

        /**
         * Obtains the caret offset and line number of the end of the last line in the current visible area.
         * Also returns an alternate (the previous line) offset/line since scrolling may not allow the caret
         * to be on the last visible line.
         *
         * @return CSV string with "offset,line,offset,line".
         */
        @JvmStatic
        fun EditorFixture.get_visible_area_bottom_offset_line(): String
        {
            val script =
                """
                    importPackage(com.intellij.util)
                    importPackage(com.intellij.openapi.editor.ex.util)
                    
                    const editor = local.get('editor')
                    const document = local.get('document')
                    
                    const is_true_smooth_scrolling_enabled = SystemProperties.isTrueSmoothScrollingEnabled()
                    const model = editor.scrollingModel
                    const visible_area = is_true_smooth_scrolling_enabled ? model.getVisibleAreaOnScrollingFinished() : model.getVisibleArea()
                    const max_Y = visible_area.y + visible_area.height - editor.getLineHeight();
                    const visible_area_bottom_line_number = editor.yToVisualLine( max_Y );
                    if( ( visible_area_bottom_line_number > 0 ) &&
                        ( max_Y < editor.visualLineToY( visible_area_bottom_line_number ) ) &&
                        ( visible_area.y <= editor.visualLineToY( visible_area_bottom_line_number - 1 ) ) )
                    {
                        visible_area_bottom_line_number--;
                    }
                    
                    const offset = document.getLineStartOffset(visible_area_bottom_line_number) +
                        EditorUtil.getLastVisualLineColumnNumber( editor,
                                                                  visible_area_bottom_line_number )
                    const alternate_offset = document.getLineStartOffset(visible_area_bottom_line_number) +
                        EditorUtil.getLastVisualLineColumnNumber( editor,
                                                                  visible_area_bottom_line_number - 1 )
                    
                    offset.toString() + ',' + visible_area_bottom_line_number.toString() + ',' +
                    alternate_offset.toString() + ',' + (visible_area_bottom_line_number - 1).toString()
                """.trimIndent();
            return callJs(script, true)
        }

        @JvmStatic
        fun EditorFixture.get_current_line_number(): Int
        {
            val script =
                """
                    const editor = local.get('editor')
                    const document = local.get('document')
                    editor.offsetToLogicalPosition(editor.getCaretModel().getOffset()).line
                """.trimIndent();
            return callJs(script, true)
        }

        @JvmStatic
        fun EditorFixture.get_line_number(offset: Int): Int
        {
            val script =
                """
                    const editor = local.get('editor')
                    editor.offsetToLogicalPosition($offset).line
                """.trimIndent();
            return callJs(script, true)
        }

        @JvmStatic
        fun EditorFixture.get_start_offset(line_number: Int): Int
        {
            val script =
                """
                    const editor = local.get('editor')
                    const document = local.get('document')
                    document.getLineStartOffset($line_number)
                """.trimIndent();
            return callJs(script, true)
        }

        @JvmStatic
        fun EditorFixture.get_end_column(line_number: Int): Int
        {
            val script =
                """
                    importPackage(com.intellij.openapi.editor.ex.util)
                                        
                    const editor = local.get('editor')
                    EditorUtil.getLastVisualLineColumnNumber( editor, $line_number )
                """.trimIndent();
            return callJs(script, true)
        }

        @JvmStatic
        fun EditorFixture.get_end_offset(line_number: Int): Int
        {
            val script =
                """
                    importPackage(com.intellij.openapi.editor.ex.util)
                                        
                    const editor = local.get('editor')
                    const document = local.get('document')
                    document.getLineStartOffset($line_number) +
                        EditorUtil.getLastVisualLineColumnNumber( editor, $line_number )
                """.trimIndent();
            return callJs(script, true)
        }

        @JvmStatic
        fun EditorFixture.get_end_offset(): Int
        {
            val script =
                """
                    const document = local.get('document')
                    document.getTextLength()
                """.trimIndent();
            return callJs(script, true)
        }
    }
}