// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package net.ddns.rkdawenterprises.brief4ijidea.plugin.pages

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.*
import com.intellij.remoterobot.search.locators.byXpath
import java.time.Duration

fun RemoteRobot.welcomeFrame(function: Welcome_frame.()-> Unit) {
    find(Welcome_frame::class.java, Duration.ofSeconds(10)).apply(function)
}

@FixtureName("Welcome Frame")
@DefaultXpath("type", "//div[@class='FlatWelcomeFrame']")
class Welcome_frame(remote_robot: RemoteRobot, remote_component: RemoteComponent) : CommonContainerFixture(remote_robot, remote_component) {
    val create_new_project_link
        get() = actionLink(byXpath("New Project","//div[(@class='MainButton' and @text='New Project') or (@accessiblename='New Project' and @class='JButton')]"))

    val more_actions
        get() = button(byXpath("More Action", "//div[@accessiblename='More Actions']"))

    val heavy_weight_popup
        get() = remoteRobot.find(ComponentFixture::class.java, byXpath("//div[@class='HeavyWeightWindow']"))

    val open_project_link
        get() = actionLink(byXpath("Open or Import",
                                   "//div[(@class='MainButton' and @text='Open') or (@accessiblename='Open or Import' and @class='JButton')]"))
}