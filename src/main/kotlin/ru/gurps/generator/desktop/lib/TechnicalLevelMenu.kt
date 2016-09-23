package ru.gurps.generator.desktop.lib

import javafx.scene.control.CheckMenuItem
import javafx.scene.control.MenuButton
import javafx.scene.control.TableView
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import java.util.*

class TechnicalLevelMenu(private val model: Model, private val tableView: TableView<Any>, private val tlMb: MenuButton) {
    private val tlList = TechnicalLevels.list
    private val tlH: HashMap<Int, String> = HashMap()

    fun set(){
        val currentTls = model.pluck("tl").distinct()
        tlList.filterKeys { currentTls.indexOf(it.toString()) != -1 }.toSortedMap().forEach {
            val cm = CheckMenuItem(it.value)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) tlH[it.key] = it.value
                else tlH.remove(it.key)

                var query = "tl in (${tlH.map { it.key }.joinToString { "$it" }})"

                if (tlH.isEmpty()) query = "tl='-3'"
                tableView.setItems(model.where(query))
            }

            tlH[it.key] = it.value
            tlMb.items.add(cm)
        }
    }
}