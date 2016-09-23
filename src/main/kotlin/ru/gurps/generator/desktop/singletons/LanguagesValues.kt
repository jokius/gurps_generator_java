package ru.gurps.generator.desktop.singletons

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.FX.Companion.messages
import tornadofx.get

object LanguagesValues {
    init { println("This Languages Types is load") }
    val spoken: ObservableList<String> = setSpoken()
    val written: ObservableList<String> = setWritten()

    private fun  setSpoken(): ObservableList<String> {
        return FXCollections.observableArrayList(messages["not_have"], messages["broken"], messages["accent"],
                messages["native"])
    }

    private fun setWritten(): ObservableList<String> {
        return FXCollections.observableArrayList(messages["illiteracy"], messages["semi-literate"],
                messages["literacy"])
    }
}
