package ru.gurps.generator.desktop.lib

import javafx.scene.control.CheckMenuItem
import javafx.scene.control.MenuButton
import javafx.scene.control.TableView
import ru.gurps.generator.desktop.config.Model
import java.util.*

class LegalityClassMenu(private val model: Model, private val tableView: TableView<Any>, private val mb: MenuButton) {
    private val list = (0..4)
    private val hash: HashMap<Int, Int> = HashMap()

    fun set(){
        val current = model.pluck("legalityClass").distinct()
        list.filter { current.indexOf(it.toString()) != -1 }.forEach {
            val cm = CheckMenuItem(it.toString())
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) hash[it] = it
                else hash.remove(it)

                var query = "legalityClass in (${hash.map { it.value }.joinToString { "$it" }})"

                if (hash.isEmpty()) query = "legalityClass='-1'"
                tableView.setItems(model.where(query))
            }

            hash[it] = it
            mb.items.add(cm)
        }
    }
}