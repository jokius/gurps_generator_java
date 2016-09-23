package ru.gurps.generator.desktop.controllers

import ru.gurps.generator.desktop.singletons.Property
import tornadofx.Rest

class NewVersionController : AppController() {
    private val git: Rest by inject()

    fun isLastVersion(): Boolean {
        try {
            git.baseURI = Property.gitAddress
            val response = git.get("releases")
            if (!response.ok()) return true

            val version = response.one().getJsonString("tag_name").string
            if (version.none()) return true
            Property.newVersionUrl = "${Property.newVersionUrl}/tag/$version"
            return version.toDouble() <= currentVersion
        } catch (e: Exception) {
            return true
        }
    }
}