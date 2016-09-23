package ru.gurps.generator.desktop.interfaces.models

import ru.gurps.generator.desktop.models.Character

interface MeleeWeaponInterface : Weapon {
    var st: Int
    var twoHands: Boolean

    fun getTwoHands(): String
    fun addToCharacter(character: Character)
    fun removeToCharacter(character: Character)
}
