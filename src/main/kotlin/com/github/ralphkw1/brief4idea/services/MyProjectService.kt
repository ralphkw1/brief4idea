package com.github.ralphkw1.brief4idea.services

import com.intellij.openapi.project.Project
import com.github.ralphkw1.brief4idea.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
