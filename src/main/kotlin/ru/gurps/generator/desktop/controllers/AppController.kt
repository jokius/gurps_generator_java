package ru.gurps.generator.desktop.controllers

import ru.gurps.generator.desktop.singletons.Property
import tornadofx.Controller
import tornadofx.JsonModel
import tornadofx.Rest
import java.util.*
import javax.json.JsonObject

open class AppController: Controller() {
    private val rest: Rest by inject()
    val currentVersion = 0.3

    fun pages(pagination: JsonObject): HashMap<String, Any> {
        val pages = HashMap<String, Any>()
        pages.put("page", pagination.getInt("current_page"))
        pages.put("next", pagination.getInt("current_page") <= pagination.getInt("total_pages"))
        return pages
    }

    fun sendRequestToServer(what: String, params: JsonModel): Rest.Response {
        rest.baseURI = Property.serverAddress
        return rest.post("/api/$what", params)
    }

    fun getPageFromServer(what: String, page: Int): JsonObject {
        rest.baseURI = Property.serverAddress
        return rest.get("/api/$what?page=$page").one()
    }
}