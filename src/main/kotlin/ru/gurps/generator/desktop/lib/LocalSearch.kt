package ru.gurps.generator.desktop.lib

import javafx.collections.ObservableList
import javafx.scene.control.TextField
import ru.gurps.generator.desktop.config.Model

class LocalSearch(private val model: Model, private val searchText: TextField) {
    fun searchAll(): ObservableList<Any> {
        val query = "UPPER(name) like UPPER('%" + searchText.text + "%') or " +
                "UPPER(nameEn) like UPPER('%" + searchText.text + "%') or " +
                "UPPER(cost) like UPPER('%" + searchText.text + "%') or " +
                "UPPER(description) like UPPER('%" + searchText.text + "%')"
        return model.where(query)
    }

    fun searchName(): ObservableList<Any> {
        val query = "UPPER(name) like UPPER('%" + searchText.text + "%')"
        return model.where(query)
    }

    fun searchNameEn(): ObservableList<Any> {
        val query = "UPPER(nameEn) like UPPER('%" + searchText.text + "%')"
        return model.where(query)
    }

    fun searchCost(): ObservableList<Any> {
        val query = "UPPER(cost) like UPPER('%" + searchText.text + "%')"
        return model.where(query)
    }

    fun searchDescription(): ObservableList<Any> {
        val query = "UPPER(description) like UPPER('%" + searchText.text + "%')"
        return model.where(query)
    }
}