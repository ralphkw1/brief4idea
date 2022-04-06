@file:Suppress("ClassName",
               "unused")

package net.ddns.rkdawenterprises.brief4ijidea

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.Messages"

object Messages : DynamicBundle(BUNDLE)
{
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String,
                vararg params: Any) =
        getMessage(key,
                   *params)

    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String,
                       vararg params: Any) =
        getLazyMessage(key,
                       *params)
}
