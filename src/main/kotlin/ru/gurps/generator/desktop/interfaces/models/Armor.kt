package ru.gurps.generator.desktop.interfaces.models

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.models.rules.ArmorsAddon

interface Armor : Inventory {
    var slot: String
    var name: String
    var protection: String
    var damageResist: String
    var cents: Long
    var cost: String
    var weight: String
    var legalityClass: Int
    val addons: ObservableList<ArmorsAddon>

    fun addToCharacter(character: Character)
    fun removeToCharacter(character: Character)
}
