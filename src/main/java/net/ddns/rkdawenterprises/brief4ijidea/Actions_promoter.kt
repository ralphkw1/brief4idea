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
import com.intellij.psi.PsiFile
import net.ddns.rkdawenterprises.brief4ijidea.actions.Plugin_action

class Actions_promoter : ActionPromoter
{
    private fun should_promote(an_action: AnAction,
                               a_PSI_file: PsiFile?): Boolean = ((an_action is Plugin_action) && (a_PSI_file != null));

    override fun promote(actions: MutableList<out AnAction>,
                         context: DataContext): MutableList<AnAction>
    {
        // TODO: Is this the best way to know the action was in a text editor and not in the IDE?
        val a_PSI_file: PsiFile?  = context.getData(CommonDataKeys.PSI_FILE);

        return actions.firstOrNull { should_promote(it, a_PSI_file); }
            ?.let { mutableListOf(it); }
            ?: mutableListOf();
    }
}