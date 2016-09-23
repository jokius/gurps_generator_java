package ru.gurps.generator.desktop.interfaces.models

import ru.gurps.generator.desktop.models.Character

interface Transport : Inventory {
    var skills: String
    var name: String
    var stHp: Int
    var handling: Int
    var stabilityRating: String
    var ht: String
    var move: String
    var loadedWeight: Double
    var load: Double
    var sizeModifier: String
    var occupant: String
    var damageResist: String
    var range: String
    var cents: Long
    var locations: String
    val locationsFull: String

    fun addToCharacter(character: Character)
    fun removeToCharacter(character: Character)
}
