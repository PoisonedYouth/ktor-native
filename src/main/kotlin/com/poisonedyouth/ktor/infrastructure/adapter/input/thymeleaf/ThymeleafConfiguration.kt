package com.poisonedyouth.ktor.infrastructure.adapter.input.thymeleaf

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.thymeleaf.Thymeleaf
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver


fun Application.configureThymeleaf(){
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver(this.javaClass.classLoader).apply {
            prefix = "templates/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
}