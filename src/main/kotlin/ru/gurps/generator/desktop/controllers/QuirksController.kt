package ru.gurps.generator.desktop.controllers

import ru.gurps.generator.desktop.models.rules.Quirk
import ru.gurps.generator.desktop.pojo.QuirkJson
import javax.json.JsonObject


class QuirksController: AppController() {
    fun updateFromServer() {

        var json = getPageFromServer("data/quirks", 1)
        if(json.isEmpty()) return
        var next = newQuirks(json)
        var pages = pages(json.getJsonObject("pagination"))
        if (next) {
            while (next && pages["next"] as Boolean) {
                json = getPageFromServer("data/quirks", pages["page"] as Int + 1)
                pages = pages(json.getJsonObject("pagination"))
                next = newQuirks(json)
            }
        }
    }

     fun sedToServer(name: String) {
        val response = sendRequestToServer("data/quirks", QuirkJson(name))
         if(response.statusCode == 204) return
         val error = response.one().getJsonString("error")
         println(error)
    }

    private fun newQuirks(json: JsonObject): Boolean {
        if (json.getJsonArray("quirks").isEmpty()) return false

        json.getJsonArray("quirks").forEach {
            val name = (it as JsonObject).getString("name")
            if ((Quirk().find_by("name", name) as Quirk).id > 0) return false
            Quirk(name).create()
        }

        return true
    }


}