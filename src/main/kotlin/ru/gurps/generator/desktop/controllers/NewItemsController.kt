package ru.gurps.generator.desktop.controllers

import ru.gurps.generator.desktop.singletons.Property
import tornadofx.Rest
import javax.json.JsonObject

class NewItemsController: AppController() {
    val items: Rest by inject()

    fun haveUpdates(): Boolean {
        items.baseURI = Property.serverAddress
        val response = items.get("api/data/change_log/status?start=${Property.updateStart}&version=$currentVersion")
        if(!response.ok()) return false
        return response.one().getBoolean("present", false)
    }

    fun updateFromServer(): JsonObject {
        val response = items.get("api/data/change_log?start=${Property.updateStart}&version=$currentVersion")
        return response.one()
    }
}