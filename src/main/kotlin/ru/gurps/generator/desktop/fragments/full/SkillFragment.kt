package ru.gurps.generator.desktop.fragments.full

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.models.characters.CharactersSpecialization
import ru.gurps.generator.desktop.models.rules.Specialization
import ru.gurps.generator.desktop.singletons.CharacterParams
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import ru.gurps.generator.desktop.views.tables.SkillsView
import tornadofx.*
import java.util.*

class SkillFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/SkillFragment.fxml")
    private val skillsView: SkillsView by inject()
    private val character = Characters.current!!
    private val skill = skillsView.currentSkill
    private val name: Label by fxid()
    private val hBox: HBox by fxid()
    private val hBoxButton: HBox by fxid()
    private val scroll: ScrollPane by fxid()
    private val fullDescription: TextFlow by fxid()
    private val finalCostLabel = label {
        minWidth = NodeAttributes.minWidth
        textAlignment = TextAlignment.CENTER
    }

    private val addButton: Button = Button(messages["add"])
    private val removeButton: Button = Button(messages["remove"])

    private val specializationsTableView: TableView<Specialization> by fxid()
    private val specializationName: TableColumn<Specialization, String> by fxid()
    private val specializationNameEn: TableColumn<Specialization, String> by fxid()
    private val specializationTypeAndComplexityColumn: TableColumn<Specialization, String> by fxid()
    private val specializationLevel: TableColumn<Specialization, Boolean> by fxid()
    private val specializationCost: TableColumn<Specialization, Boolean> by fxid()
    private val activate: TableColumn<Specialization, Boolean> by fxid()

    init {
        name.text = "${skill.name} (${skill.nameEn})"
        fullDescription.children.addAll(type(), complexity(), defaultUse(), demands(), modifiers(), description())
        intHBox()
        setButtons()
        addSpecializations()
    }

    private fun intHBox() {
        hBox.children.clear()
        hBoxButton.children.clear()
        addLevel()
        addFinalCost()
        addButton()
    }

    private fun addLevel() {
        if (skill.add || skill.specializations.any()) {
            hBox.children.add(lvlLabel(skill.level.toString()))
            return
        }

        hBox.children.add(lvlText(skill.level.toString()))
    }

    private fun addFinalCost() {
        finalCostLabel.text = if (skill.add) skill.cost.toString()
        else CharacterParams.skillCost(skill).toString()
        hBox.children.add(finalCostLabel)
    }

    private fun addButton() {
        if (skill.specializations.any()) return
        if (skill.add) hBoxButton.children.add(removeButton)
        else hBoxButton.children.add(addButton)
    }

    private fun setButtons() {
        addButton.setOnAction {
            skillsView.callAddOrUpdate(skillsView.currentCell)
            skill.add = true
            specializationsTableView.refresh()
            intHBox()
        }

        removeButton.setOnAction {
            skillsView.callRemove(skillsView.currentCell)
            skill.add = false
            specializationsTableView.refresh()
            newCost(0)
            intHBox()
        }
    }

    private fun lvlText(level: String): TextField {
        return textfield(level) {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER

            textProperty().addListener { _, old, value ->
                if (!Regex("\\d+").matches(value) || old == value || value.isNullOrBlank()) return@addListener
                skill.level = value.toInt()
                skill.cost = CharacterParams.skillCost(skill)
                newCost(skill.cost)
            }
        }
    }

    private fun lvlLabel(level: String): Label {
        return label(level) {
            minWidth = NodeAttributes.minWidth
            textAlignment = TextAlignment.CENTER
        }
    }

    private fun newCost(changeCost: Int) {
        val result: String = changeCost.toString()
        finalCostLabel.text = result
    }

    private fun addSpecializations() {
        if (skill.specializations.isEmpty()) return
        setSpecializations()
        setItems()
        showTable()
    }

    private fun showTable() {
        specializationsTableView.isVisible = true
        fullDescription.prefHeight = fullDescription.prefHeight - specializationsTableView.prefHeight
        AnchorPane.setTopAnchor(scroll, AnchorPane.getTopAnchor(scroll) + specializationsTableView.prefHeight)
    }

    private fun setItems() {
        val params = HashMap<String, Any>()
        params["characterId"] = character.id
        params["skillId"] = skill.id

        skill.specializations.forEach {
            params.put("itemId", it.id)
            val charactersSpecialization = CharactersSpecialization().find_by(params) as CharactersSpecialization
            it.level = CharacterParams.skillLevel(it)
            if (charactersSpecialization.id != -1) {
                it.cost = charactersSpecialization.cost
                it.level = charactersSpecialization.level
                it.add = true
            }
        }

        specializationsTableView.items = skill.specializations
    }

    private fun setSpecializations() {
        specializationName.cellValueFactory = PropertyValueFactory("name")
        specializationNameEn.cellValueFactory = PropertyValueFactory("nameEn")
        specializationLevel.setCellValueFactory { SimpleBooleanProperty(true) }
        specializationTypeAndComplexityColumn.cellValueFactory = PropertyValueFactory("typeAndComplexity")
        specializationCost.setCellValueFactory { SimpleBooleanProperty(true) }
        activate.setCellValueFactory { SimpleBooleanProperty(true) }
        activate.setCellFactory { ButtonCell() }
        specializationLevel.setCellFactory { LevelCell() }
        specializationCost.setCellFactory { CostCell() }
    }

    inner class LevelCell : TableCell<Specialization, Boolean>() {
        private val textField = textfield {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER
        }

        private val label = label {
            minWidth = NodeAttributes.minWidth
            textAlignment = TextAlignment.CENTER
        }

        override fun updateItem(item: Boolean?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                graphic = null
                return
            }

            if (rowItem.add) {
                label.text = rowItem.level.toString()
                graphic = label
                return
            }

            textField.text = rowItem.level.toString()
            textField.textProperty().addListener { _, _, value ->
                if (!Regex("\\d+").matches(value) || rowItem.level.toString() == value ||
                        value.isNullOrBlank()) return@addListener
                rowItem.level = value.toInt()
                rowItem.cost = CharacterParams.skillCost(rowItem)
                @Suppress("UNCHECKED_CAST")
                val col: TableColumn<Specialization, Boolean> = tableView.columns.find { it == specializationCost } as
                        TableColumn<Specialization, Boolean>
                col.isVisible = false
                col.isVisible = true
                textField.positionCaret(value.length)
            }

            graphic = textField
        }
    }

    inner class CostCell : TableCell<Specialization, Boolean>() {
        private val label = label {
            minWidth = NodeAttributes.minWidth
            textAlignment = TextAlignment.CENTER
        }

        override fun updateItem(item: Boolean?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                graphic = null
                return
            }

            label.text = if (rowItem.add) rowItem.cost.toString()
            else CharacterParams.skillCost(rowItem).toString()
            graphic = label
        }
    }

    inner class ButtonCell : TableCell<Specialization, Boolean>() {
        private var addButtonAddon = button(messages["add"]) {
            isVisible = false
            minWidth = 84.0
            setOnAction { addSpecializationCall() }
        }
        private var removeButtonAddon = button(messages["remove"]) {
            isVisible = false
            minWidth = 84.0
            setOnAction { removeSpecializationCall() }
        }

        private val paddedButtonAddon = stackpane {
            children.addAll(addButtonAddon, removeButtonAddon)
        }
        private val hBoxAddon = hbox {
            alignment = Pos.CENTER
            children.add(paddedButtonAddon)
        }

        private fun initButtons() {
            if (rowItem.add) {
                removeButtonAddon.isVisible = true
                tableRow.styleClass.add("isAdd")
            } else {
                addButtonAddon.isVisible = true
                tableRow.styleClass.remove("isAdd")
            }
        }

        private fun addSpecializationCall() {
            val specialization = rowItem
            specialization.add = true
            specializationCost(specialization)
            CharactersSpecialization(characterId = character.id, skillId = skill.id, itemId = specialization.id,
                    level = specialization.level, cost = specialization.cost).create()
            skillsView.callAddOrUpdate(skillsView.currentCell)
            skill.add = true
            specializationsTableView.refresh()
        }

        private fun removeSpecializationCall() {
            val specialization = rowItem
            specialization.add = false
            val params = HashMap<String, Any>()
            params.put("characterId", character.id)
            params.put("itemId", specialization.id)
            val charactersSpecialization = CharactersSpecialization().find_by(params) as CharactersSpecialization
            charactersSpecialization.destroy()
            specializationCost(specialization)
            skillsView.callAddOrUpdate(skillsView.currentCell)
            if (charactersSpecializations().isEmpty()) {
                skillsView.callRemove(skillsView.currentCell)
                skill.add = false
            }
            specializationsTableView.refresh()
        }

        private fun charactersSpecializations(): ObservableList<Specialization> {
            val params = HashMap<String, Any>()
            params.put("characterId", character.id)
            params.put("skillId", skill.id)
            return character.specializations(params)
        }

        private fun specializationCost(specialization: Specialization) {
            if (specialization.add) {
                skill.cost += specialization.cost
            } else {
                skill.cost -= specialization.cost
            }

            newCost(skill.cost)
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            initButtons()
            graphic = hBoxAddon
        }
    }

    private fun type(): Text {
        val type = Text(skill.typeFull + "\r\n")
        type.addClass("type")
        return type
    }

    private fun complexity(): Text {
        val complexity = Text(skill.complexityFull + "\r\n")
        complexity.addClass("complexity")
        return complexity
    }

    private fun defaultUse(): Text {
        val defaultUse = Text("${messages["default"]}: ${skill.defaultUse}" + "\r\n")
        defaultUse.addClass("defaultUse")
        return defaultUse
    }

    private fun demands(): Text {
        val demands = Text("${messages["demands"]}: ${skill.demands}" + "\r\n")
        demands.addClass("demands")
        return demands
    }

    private fun modifiers(): Text {
        val modifiers = Text("${messages["modifiers"]}: ${skill.modifiers}" + "\r\n\r\n")
        modifiers.addClass("modifiers")
        return modifiers
    }

    private fun description(): Text {
        var fullDescription = skill.description + "\r\n"
        skill.specializations.forEach {
            fullDescription += "\r\n${it.name}(${it.nameEn})\r\n"
            fullDescription += it.description + "\r\n"
        }

        val description = Text(fullDescription)
        description.addClass("description")
        return description
    }
}
