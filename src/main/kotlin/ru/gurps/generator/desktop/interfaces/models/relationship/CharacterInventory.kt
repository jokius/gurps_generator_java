package ru.gurps.generator.desktop.interfaces.models.relationship

interface CharacterInventory {
    var id: Int
    var characterId: Int
    var itemId: Int
    var count: Int
}