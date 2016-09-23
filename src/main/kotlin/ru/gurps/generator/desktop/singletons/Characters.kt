package ru.gurps.generator.desktop.singletons

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.gurps.generator.desktop.models.Character

object Characters {
    init { println("This Character is load") }
    var current:Character? = null
    var all: ObservableList<Character> = FXCollections.observableArrayList()
}
