@file:Suppress("ClassName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.project.DumbAwareAction

/**
 * Parent for all plugin defined actions so they can be identified by the Actions Promoter.
 *
 * @constructor
 *
 * @param text
 * @param description
 */
abstract class Plugin_action(text: String?,
                             description: String?) : DumbAwareAction(text,
                                                                     description,
                                                                     null)
