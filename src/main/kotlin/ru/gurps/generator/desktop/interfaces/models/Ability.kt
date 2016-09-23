package ru.gurps.generator.desktop.interfaces.models

interface Ability : Base {
    var name: String
    var nameEn: String
    var cost: Int
    var currentCost: Int
    var description: String
    var add: Boolean
}