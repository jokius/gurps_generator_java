package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.singletons.Money
import tornadofx.*
import tornadofx.FX.Companion.messages
import java.sql.ResultSet

class Alchemy : Model {
    override var id: Int = -1
    var name: String = ""
    var nameEn: String = ""
    var alternativeNames: String = ""
    var description: String = ""
    var duration: String = ""
    var form: String = ""
    var cost: String = ""
    var recipe: String = ""
    var recipeCost: Long = 0
    var recipeCostDollars: String = ""
        get() = Money.toDollars(recipeCost)
    var add: Boolean = false
    val alternativeNamesSingle: String
        get() = if (alternativeNames.isBlank()) "" else "${messages["alternative_names"]}: $alternativeNames"
    val durationSingle: String
        get() {
            val dur = if (duration.isBlank()) messages["no"] else duration
            return "${messages["duration"]}: $dur"
        }
    val formSingle: String
        get() {
            val formS = if (form.isBlank()) messages["no"] else form
            return "${messages["form"]}: $formS"
        }
    val recipeSingle: String
        get() {
            val rec = if (recipe.isBlank()) messages["no"] else recipe
            return "${messages["recipe"]}: $rec"
        }

    val recipeCostSingle: String
        get() = "${messages["recipe_cost"]}: $recipeCost"

    val costSingle: String
        get() {
            val costS = if (cost.isBlank()) messages["no"] else cost
            return "${messages["cost"]}: $costS"
        }

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val nameEnRes = result.getString("nameEn")
        if (nameEnRes != null) nameEn = nameEnRes
        val alternativeNamesRes = result.getString("alternativeNames")
        if (alternativeNamesRes != null) alternativeNames = alternativeNamesRes
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
        val durationRes = result.getString("duration")
        if (durationRes != null) duration = durationRes
        val formRes = result.getString("form")
        if (formRes != null) form = formRes
        val costRes = result.getString("cost")
        if (costRes != null) cost = costRes
        val recipeRes = result.getString("recipe")
        if (recipeRes != null) recipe = recipeRes
        recipeCost = result.getLong("recipeCost")
    }
}
