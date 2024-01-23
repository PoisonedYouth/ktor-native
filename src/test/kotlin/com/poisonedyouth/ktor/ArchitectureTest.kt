package com.poisonedyouth.ktor

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import org.junit.jupiter.api.Test

class ArchitectureTest {

    @Test
    fun `verify hexagonal architecture`(){
        Konsist
            .scopeFromProject()
            .assertArchitecture {
                val domain = Layer("domain", "com.poisonedyouth.ktor.domain..")
                val application = Layer("application", "com.poisonedyouth.ktor.application..")
                val infrastructure = Layer("infrastructure", "com.poisonedyouth.ktor.infrastructure..")

                domain.dependsOnNothing()
                application.dependsOn(domain)
                infrastructure.dependsOn(domain, application)
            }
    }
}