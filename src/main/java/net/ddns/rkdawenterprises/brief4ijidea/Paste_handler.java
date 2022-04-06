// Portions copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

// TODO: com.intellij.ide.actions.PasteReferenceProvider
// TODO: com.intellij.codeInsight.editorActions.PasteHandler
// TODO: com.intellij.codeInsight.editorActions.DefaultTypingActionsExtension

package net.ddns.rkdawenterprises.brief4ijidea;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.editorActions.*;
import com.intellij.ide.PasteProvider;
import com.intellij.lang.LanguageFormatting;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.impl.UndoManagerImpl;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actions.BasePasteHandler;
import com.intellij.openapi.editor.actions.EditorActionUtil;
import com.intellij.openapi.editor.actions.PasteAction;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.DocumentUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Producer;
import com.intellij.util.text.CharArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.*;

/**
 * This handler is used to move the cursor to the beginning of the current line when pasting clipboard content
 * that are lines (ends in line termination), with no active selection. This could be done in the paste action, but then
 * the language formatters would not kick in, nor would it work with the paste-multiple dialog. But this method seems to
 * work for all that.
 * It also handles the Brief emulation column marking mode clipboard content.
 */
public class Paste_handler
        implements PasteProvider
{
    @Override
    public void performPaste( @NotNull DataContext dataContext )
    {
        final Project project = CommonDataKeys.PROJECT.getData( dataContext );
        final Editor editor = CommonDataKeys.EDITOR.getData( dataContext );
        final PsiFile file = CommonDataKeys.PSI_FILE.getData( dataContext );
        final Transferable transferable = CopyPasteManager.getInstance()
                                                          .getContents();
        if( ( project != null ) && ( editor != null ) && ( file != null ) && ( transferable != null ) )
        {
            do_paste( editor,
                      project,
                      file,
                      editor.getDocument(),
                      transferable );
        }
    }

    /**
     * Should perform fast and memory cheap negation. May return incorrect true. See #12326
     *
     * @param dataContext The data context
     */
    @Override
    public boolean isPastePossible( @NotNull DataContext dataContext )
    {
        return is_good_to_go( dataContext );
    }

    @Override
    public boolean isPasteEnabled( @NotNull DataContext dataContext )
    {
        return is_good_to_go( dataContext );
    }

    private boolean is_good_to_go( @NotNull DataContext dataContext )
    {
        final Project project = CommonDataKeys.PROJECT.getData( dataContext );
        final Editor editor = CommonDataKeys.EDITOR.getData( dataContext );
        final PsiFile file = CommonDataKeys.PSI_FILE.getData( dataContext );
        String paste_data = get_paste_data( dataContext );

        if( ( project != null ) && ( editor != null ) && ( file != null ) && !file.getFileType()
                                                                                  .isBinary() && ( paste_data != null ) && ( paste_data.length() > 0 ) )
        {
            if( paste_data.contains( Column_marking_component.Column_mode_block_transferable.get_mime_type() ) )
            {
                return true;
            }
            else
            {
                return State_component.get_instance()
                                                .get_paste_lines_at_home() && !editor.getSelectionModel()
                                                                                     .hasSelection() &&
                        ( paste_data.endsWith( "\n" ) || paste_data.endsWith( "\r" ) || paste_data.endsWith( "\r\n" ) || paste_data.endsWith( "\n\r" ) );
            }
        }

        return false;
    }

    @Nullable
    private static String get_paste_data( final DataContext context )
    {
        Producer<Transferable> producer = PasteAction.TRANSFERABLE_PROVIDER.getData( context );

        if( producer != null )
        {
            Transferable transferable = producer.produce();
            if( transferable != null )
            {
                try
                {
                    return (String)transferable.getTransferData( DataFlavor.stringFlavor );
                }
                catch( Exception ignored ) { }
            }
            return null;
        }

        return CopyPasteManager.getInstance()
                               .getContents( DataFlavor.stringFlavor );
    }

    private static void do_paste( @NotNull final Editor editor,
                                  @NotNull final Project project,
                                  final PsiFile file,
                                  final Document document,
                                  @NotNull final Transferable content )
    {
        CopyPasteManager.getInstance()
                        .stopKillRings();

        String transfer_data = null;
        try
        {
            transfer_data = (String)content.getTransferData( DataFlavor.stringFlavor );
        }
        catch( Exception e )
        {
            editor.getComponent()
                  .getToolkit()
                  .beep();
        }

        if( transfer_data == null ) return;

        if( BasePasteHandler.isContentTooLarge( transfer_data.length() ) )
        {
            BasePasteHandler.contentLengthLimitExceededMessage( transfer_data.length() );
            return;
        }

        final CodeInsightSettings code_insight_settings = CodeInsightSettings.getInstance();

        // noinspection rawtypes
        final Map<CopyPastePostProcessor, List<? extends TextBlockTransferableData>> text_block_transferable_data_map = new HashMap<>();
        final Collection<TextBlockTransferableData> text_block_transferable_data_collection = new ArrayList<>();

        for( CopyPastePostProcessor<? extends TextBlockTransferableData> processor : CopyPastePostProcessor.EP_NAME.getExtensionList() )
        {
            List<? extends TextBlockTransferableData> data = processor.extractTransferableData( content );
            if( !data.isEmpty() )
            {
                text_block_transferable_data_map.put( processor,
                                                      data );
                text_block_transferable_data_collection.addAll( data );
            }
        }

        transfer_data = TextBlockTransferable.convertLineSeparators( editor,
                                                                     transfer_data,
                                                                     text_block_transferable_data_collection );

        final CaretModel caret_model = editor.getCaretModel();
        final SelectionModel selection_model = editor.getSelectionModel();
        int logical_position_column = caret_model.getLogicalPosition().column;

        final int block_indent_anchor_column;
        final int caret_offset = caret_model.getOffset();
        if( selection_model.hasSelection() && caret_offset >= selection_model.getSelectionStart() )
        {
            block_indent_anchor_column = editor.offsetToLogicalPosition( selection_model.getSelectionStart() ).column;
        }
        else
        {
            if( !transfer_data.contains( Column_marking_component.Column_mode_block_transferable.get_mime_type() ) )
            {
                // The pasted content are full lines, so paste at the beginning of the line.
                if( logical_position_column != 0 )
                {
                    EditorActionUtil.moveCaretToLineStart( editor,
                                                           false );
                    logical_position_column = editor.getCaretModel()
                                                    .getLogicalPosition().column;
                }
            }

            block_indent_anchor_column = logical_position_column;
        }

        RawText raw_text_from_transferable = RawText.fromTransferable( content );
        String preprocess_on_paste_text = transfer_data;
        for( CopyPastePreProcessor copy_paste_preprocessor : CopyPastePreProcessor.EP_NAME.getExtensionList() )
        {
            preprocess_on_paste_text = copy_paste_preprocessor.preprocessOnPaste( project,
                                                                                  file,
                                                                                  editor,
                                                                                  preprocess_on_paste_text,
                                                                                  raw_text_from_transferable );
        }

        final boolean preprocess_on_paste_text_changed = !transfer_data.equals( preprocess_on_paste_text );
        int indent_type = preprocess_on_paste_text_changed ? CodeInsightSettings.REFORMAT_BLOCK : code_insight_settings.REFORMAT_ON_PASTE;
        transfer_data = preprocess_on_paste_text;

        if( LanguageFormatting.INSTANCE.forContext( file ) == null && indent_type != CodeInsightSettings.NO_REFORMAT )
        {
            indent_type = CodeInsightSettings.INDENT_BLOCK;
        }

        if( transfer_data.contains( Column_marking_component.Column_mode_block_transferable.get_mime_type() ) )
        {
            // Pasting column marking mode content. Pasting "as-is", no further processing.
            Column_marking_component.paste( project,
                                            editor,
                                            transfer_data );
            return;
        }

        final String final_text = transfer_data;
        ApplicationManager.getApplication()
                          .runWriteAction( () ->
                                           {
                                               EditorModificationUtil.insertStringAtCaret( editor,
                                                                                           final_text,
                                                                                           false,
                                                                                           true );
                                               if( !project.isDisposed() )
                                               {
                                                   ( (UndoManagerImpl)UndoManager.getInstance( project ) ).addDocumentAsAffected( editor.getDocument() );
                                               }
                                           } );

        int transfer_data_length = transfer_data.length();
        int offset = caret_model.getOffset() - transfer_data_length;

        if( offset < 0 )
        {
            transfer_data_length += offset;
            offset = 0;
        }

        final RangeMarker bounds = document.createRangeMarker( offset,
                                                               offset + transfer_data_length );

        caret_model.moveToOffset( bounds.getEndOffset() );
        editor.getScrollingModel()
              .scrollToCaret( ScrollType.RELATIVE );
        selection_model.removeSelection();

        final Ref<Boolean> do_not_indent = new Ref<>( preprocess_on_paste_text_changed ? Boolean.FALSE : null );

        // noinspection rawtypes
        for( Map.Entry<CopyPastePostProcessor, List<? extends TextBlockTransferableData>> text_block_transferable_data_entry : text_block_transferable_data_map.entrySet() )
        {
            //noinspection unchecked
            text_block_transferable_data_entry.getKey()
                                              .processTransferableData( project,
                                                                        editor,
                                                                        bounds,
                                                                        caret_offset,
                                                                        do_not_indent,
                                                                        text_block_transferable_data_entry.getValue() );
        }

        boolean pasted_text_is_whitespace =
                CharArrayUtil.shiftForward( document.getCharsSequence(),
                                            bounds.getStartOffset(),
                                            " \n\t" ) >= bounds.getEndOffset();

        VirtualFile virtualFile = file.getVirtualFile();
        if( !pasted_text_is_whitespace &&
                ( virtualFile == null || !SingleRootFileViewProvider.isTooLargeForIntelligence( virtualFile ) ) )
        {
            final boolean do_not_reformat = ( do_not_indent.get() == Boolean.TRUE )
                    && ( ( indent_type == CodeInsightSettings.INDENT_BLOCK ) || ( indent_type == CodeInsightSettings.INDENT_EACH_LINE ) );
            final int reformat_type = do_not_reformat ? CodeInsightSettings.NO_REFORMAT : indent_type;
            ApplicationManager.getApplication()
                              .runWriteAction(
                                      () -> format( project,
                                                    editor,
                                                    reformat_type,
                                                    bounds.getStartOffset(),
                                                    bounds.getEndOffset(),
                                                    block_indent_anchor_column,
                                                    true )
                                             );
        }

        if( bounds.isValid() )
        {
            caret_model.moveToOffset( bounds.getEndOffset() );
            editor.getScrollingModel()
                  .scrollToCaret( ScrollType.RELATIVE );
            selection_model.removeSelection();
            editor.putUserData( EditorEx.LAST_PASTED_REGION,
                                TextRange.create( bounds ) );
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void format( @NotNull Project project,
                                @NotNull Editor editor,
                                int reformat_type,
                                int start_offset,
                                int end_offset,
                                int anchor_column,
                                boolean indent_before_reformat )
    {
        PsiDocumentManager.getInstance( project )
                          .doPostponedOperationsAndUnblockDocument( editor.getDocument() );

        switch( reformat_type )
        {
            case CodeInsightSettings.INDENT_BLOCK:
            {
                indent_block( project,
                              editor,
                              start_offset,
                              end_offset,
                              anchor_column );
                break;
            }

            case CodeInsightSettings.INDENT_EACH_LINE:
            {
                indent_each_line( project,
                                  editor,
                                  start_offset,
                                  end_offset );
                break;
            }

            case CodeInsightSettings.REFORMAT_BLOCK:
            {
                final RangeMarker bounds = editor.getDocument()
                                                 .createRangeMarker( start_offset,
                                                                     end_offset );
                if( indent_before_reformat )
                {
                    indent_each_line( project,
                                      editor,
                                      start_offset,
                                      end_offset );
                }

                reformat_range( project,
                                editor,
                                bounds.getStartOffset(),
                                bounds.getEndOffset() );

                bounds.dispose();

                break;
            }
        }
    }

    private static void indent_block( @NotNull Project project,
                                      @NotNull Editor editor,
                                      int startOffset,
                                      int endOffset,
                                      int originalCaretCol )
    {
        final PsiDocumentManager documentManager = PsiDocumentManager.getInstance( project );
        final Document document = editor.getDocument();
        PsiFile file = documentManager.getPsiFile( document );
        if( file == null )
        {
            return;
        }

        if( LanguageFormatting.INSTANCE.forContext( file ) != null )
        {
            indent_block_with_formatter( project,
                                         editor,
                                         startOffset,
                                         endOffset );
        }
        else
        {
            indent_plain_text_block( document,
                                     startOffset,
                                     endOffset,
                                     originalCaretCol );
        }
    }

    private static void indent_plain_text_block( final @NotNull Document document,
                                                 final int startOffset,
                                                 final int endOffset,
                                                 final int indentLevel )
    {
        CharSequence chars = document.getCharsSequence();
        int spaceEnd = CharArrayUtil.shiftForward( chars,
                                                   startOffset,
                                                   " \t" );
        final int startLine = document.getLineNumber( startOffset );
        if( spaceEnd > endOffset || indentLevel <= 0 || startLine >= document.getLineCount() - 1 || chars.charAt( spaceEnd ) == '\n' )
        {
            return;
        }

        int endLine = startLine + 1;
        while( endLine < document.getLineCount() && document.getLineStartOffset( endLine ) < endOffset ) endLine++;

        final String indentString = StringUtil.repeatSymbol( ' ',
                                                             indentLevel );
        indent_lines( document,
                      startLine + 1,
                      endLine - 1,
                      indentString );
    }

    private static void indent_each_line( @NotNull Project project,
                                          @NotNull Editor editor,
                                          int startOffset,
                                          int endOffset )
    {
        final CharSequence text = editor.getDocument()
                                        .getCharsSequence();
        if( startOffset > 0 && endOffset > startOffset + 1 && text.charAt( endOffset - 1 ) == '\n' && text.charAt( startOffset - 1 ) == '\n' )
        {
            endOffset--;
        }
        adjust_line_indent( project,
                            editor,
                            startOffset,
                            endOffset );
    }

    private static void reformat_range( @NotNull Project project,
                                        @NotNull Editor editor,
                                        int startOffset,
                                        int endOffset )
    {
        Document document = editor.getDocument();
        final PsiDocumentManager documentManager = PsiDocumentManager.getInstance( project );
        documentManager.commitDocument( document );
        PsiFile file = PsiDocumentManager.getInstance( project )
                                         .getPsiFile( document );
        if( file == null ) return;
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance( project );
        try
        {
            codeStyleManager.reformatRange( file,
                                            startOffset,
                                            endOffset,
                                            true );
        }
        catch( IncorrectOperationException ignored ) { }
    }

    private static final int LINE_LIMIT_FOR_BULK_CHANGE = 5000;

    private static void indent_block_with_formatter( @NotNull Project project,
                                                     @NotNull Editor editor,
                                                     int startOffset,
                                                     int endOffset )
    {
        final Document document = editor.getDocument();
        final CharSequence chars = document.getCharsSequence();
        final int firstLine = document.getLineNumber( startOffset );
        final int firstLineStart = document.getLineStartOffset( firstLine );

        boolean saveLastLineIndent = false;
        for( int i = endOffset - 1; i >= startOffset; i-- )
        {
            final char c = chars.charAt( i );
            if( c == '\n' )
            {
                saveLastLineIndent = true;
                break;
            }
            if( c != ' ' && c != '\t' )
            {
                break;
            }
        }

        final int lastLine;
        if( saveLastLineIndent )
        {
            lastLine = document.getLineNumber( endOffset ) - 1;

            int start = document.getLineStartOffset( lastLine + 1 );
            if( start < endOffset )
            {
                int i = CharArrayUtil.shiftForward( chars,
                                                    start,
                                                    " \t" );
                if( i > start )
                {
                    i = Math.min( i,
                                  endOffset );
                    document.deleteString( start,
                                           i );
                }
            }

            int indentToKeepEndOffset = Math.min( startOffset,
                                                  CharArrayUtil.shiftForward( chars,
                                                                              firstLineStart,
                                                                              " \t" ) );
            if( indentToKeepEndOffset > firstLineStart )
            {
                document.insertString( start,
                                       chars.subSequence( firstLineStart,
                                                          indentToKeepEndOffset ) );
            }
        }
        else
        {
            lastLine = document.getLineNumber( endOffset );
        }

        final int i = CharArrayUtil.shiftBackward( chars,
                                                   startOffset - 1,
                                                   " \t" );

        if( chars.charAt( startOffset ) != '\n' && i > 0 && chars.charAt( i ) != '\n' )
        {
            int firstNonWsOffset = CharArrayUtil.shiftForward( chars,
                                                               firstLineStart,
                                                               " \t" );
            if( firstNonWsOffset > firstLineStart )
            {
                CharSequence toInsert = chars.subSequence( firstLineStart,
                                                           firstNonWsOffset );
                indent_lines( document,
                              firstLine + 1,
                              lastLine,
                              toInsert );
            }
            return;
        }

        final int j = CharArrayUtil.shiftForward( chars,
                                                  startOffset,
                                                  " \t\n" );
        if( j >= endOffset )
        {
            return;
        }

        final int anchorLine = document.getLineNumber( j );
        final int anchorLineStart = document.getLineStartOffset( anchorLine );
        adjust_line_indent( project,
                            editor,
                            j );

        if( anchorLine == firstLine && j == startOffset )
        {
            int indentOffset = CharArrayUtil.shiftForward( chars,
                                                           firstLineStart,
                                                           " \t" );
            if( indentOffset > firstLineStart )
            {
                CharSequence toInsert = chars.subSequence( firstLineStart,
                                                           indentOffset );
                indent_lines( document,
                              firstLine + 1,
                              lastLine,
                              toInsert );
            }
            return;
        }

        final int firstNonWsOffset = CharArrayUtil.shiftForward( chars,
                                                                 anchorLineStart,
                                                                 " \t" );
        final int diff = firstNonWsOffset - j;
        if( diff == 0 )
        {
            return;
        }
        if( diff > 0 )
        {
            CharSequence toInsert = chars.subSequence( anchorLineStart,
                                                       anchorLineStart + diff );
            indent_lines( document,
                          anchorLine + 1,
                          lastLine,
                          toInsert );
            return;
        }

        if( anchorLine == firstLine && -diff == startOffset - firstLineStart )
        {
            return;
        }
        if( anchorLine != firstLine || -diff > startOffset - firstLineStart )
        {
            final int desiredSymbolsToRemove;
            if( anchorLine == firstLine )
            {
                desiredSymbolsToRemove = -diff - ( startOffset - firstLineStart );
            }
            else
            {
                desiredSymbolsToRemove = -diff;
            }

            Runnable unindentTask = () ->
            {
                for( int line = anchorLine + 1; line <= lastLine; line++ )
                {
                    int currentLineStart = document.getLineStartOffset( line );
                    int currentLineIndentOffset = CharArrayUtil.shiftForward( chars,
                                                                              currentLineStart,
                                                                              " \t" );
                    int symbolsToRemove = Math.min( currentLineIndentOffset - currentLineStart,
                                                    desiredSymbolsToRemove );
                    if( symbolsToRemove > 0 )
                    {
                        document.deleteString( currentLineStart,
                                               currentLineStart + symbolsToRemove );
                    }
                }
            };

            DocumentUtil.executeInBulk( document,
                                        lastLine - anchorLine > LINE_LIMIT_FOR_BULK_CHANGE,
                                        unindentTask );
        }
        else
        {
            CharSequence toInsert = chars.subSequence( anchorLineStart,
                                                       diff + startOffset );
            indent_lines( document,
                          anchorLine + 1,
                          lastLine,
                          toInsert );
        }
    }

    private static void indent_lines( final @NotNull Document document,
                                      final int startLine,
                                      final int endLine,
                                      final @NotNull CharSequence indentString )
    {
        Runnable indentTask = () ->
        {
            for( int line = startLine; line <= endLine; line++ )
            {
                int lineStartOffset = document.getLineStartOffset( line );
                document.insertString( lineStartOffset,
                                       indentString );
            }
        };
        DocumentUtil.executeInBulk( document,
                                    endLine - startLine > LINE_LIMIT_FOR_BULK_CHANGE,
                                    indentTask );
    }

    private static void adjust_line_indent( @NotNull Project project,
                                            @NotNull Editor editor,
                                            int startOffset,
                                            int endOffset )
    {
        Document document = editor.getDocument();
        final PsiDocumentManager documentManager = PsiDocumentManager.getInstance( project );
        documentManager.commitDocument( document );
        PsiFile file = PsiDocumentManager.getInstance( project )
                                         .getPsiFile( document );
        if( file == null ) return;
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance( project );
        try
        {
            codeStyleManager.adjustLineIndent( file,
                                               new TextRange( startOffset,
                                                              endOffset ) );
        }
        catch( IncorrectOperationException ignored ) { }
    }

    private static void adjust_line_indent( @NotNull Project project,
                                            @NotNull Editor editor,
                                            int offset )
    {
        Document document = editor.getDocument();
        final PsiDocumentManager documentManager = PsiDocumentManager.getInstance( project );
        documentManager.commitDocument( document );
        PsiFile file = PsiDocumentManager.getInstance( project )
                                         .getPsiFile( document );
        if( file == null ) return;
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance( project );
        try
        {
            codeStyleManager.adjustLineIndent( file,
                                               offset );
        }
        catch( IncorrectOperationException ignored ) { }
    }
}
