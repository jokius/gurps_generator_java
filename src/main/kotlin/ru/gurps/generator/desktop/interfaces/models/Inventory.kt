package ru.gurps.generator.desktop.interfaces.models

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.models.rules.Note

interface Inventory : Base {
    var tl: Int
    val notes: ObservableList<Note>
    var count: Int
    var add: Boolean

    fun getFinalCost(): String
    fun getTl(): String
    fun getFinalConstCents(): Long
}
