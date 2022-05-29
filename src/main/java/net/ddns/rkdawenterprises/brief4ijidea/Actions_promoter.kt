@file:Suppress("ClassName",
               "FunctionName",
               "LocalVariableName",
               "unused",
               "RedundantSemicolon",
               "ComponentNotRegistered",
               "PrivatePropertyName")

package net.ddns.rkdawenterprises.brief4ijidea

import com.intellij.openapi.actionSystem.ActionPromoter
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import net.ddns.rkdawenterprises.brief4ijidea.actions.Plugin_action

fun should_promote(an_action: AnAction,
                   context: DataContext): Boolean
{
    val a_PSI_file: PsiFile?  = context.getData(CommonDataKeys.PSI_FILE);
    val an_editor: Editor? = context.getData(CommonDataKeys.EDITOR);

    return ((an_action is Plugin_action) && (a_PSI_file != null) && (an_editor != null));
}

class Actions_promoter : ActionPromoter
{
    override fun promote(actions: MutableList<out AnAction>,
                         context: DataContext): MutableList<AnAction>
    {
        return actions.firstOrNull { should_promote(it, context); }
            ?.let { mutableListOf(it); }
            ?: mutableListOf();
    }
}