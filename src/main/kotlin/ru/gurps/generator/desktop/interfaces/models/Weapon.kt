package ru.gurps.generator.desktop.interfaces.models

interface Weapon : Inventory {
    var skills: String
    var name: String
    var damage: String
    var weight: String
    var cents: Long
    var legalityClass: Int
}
