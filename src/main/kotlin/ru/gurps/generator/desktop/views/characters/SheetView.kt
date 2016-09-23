package ru.gurps.generator.desktop.views.characters

import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.rules.*
import ru.gurps.generator.desktop.singletons.CharacterChange
import ru.gurps.generator.desktop.singletons.CharacterParams
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class SheetView : View() {
    override val root: TabPane by fxml("/templates/characters/SheetView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()
    private val paramsView: ParamsView by inject()

    private val nameTextField: TextField by fxid()
    private val playerTextField: TextField by fxid()
    private val growthTextField: TextField by fxid()
    private val weightTextField: TextField by fxid()
    private val ageTextField: TextField by fxid()

    private val maxPointsTextField: TextField by fxid()
    private val currentPointsLabel: Label by fxid()

    private val smTextField: TextField by fxid()
    private val noFineManipulatorsCheckBox: CheckBox by fxid()

    private val tlTextField: TextField by fxid()
    private val tlCostTextField: TextField by fxid()

    private val stTextField: TextField by fxid()
    private val stCostLabel: Label by fxid()
    private val dxTextField: TextField by fxid()
    private val dxCostLabel: Label by fxid()
    private val iqTextField: TextField by fxid()
    private val iqCostLabel: Label by fxid()
    private val htTextField: TextField by fxid()
    private val htCostLabel: Label by fxid()

    private val hpTextField: TextField by fxid()
    private val hpCostLabel: Label by fxid()
    private val willTextField: TextField by fxid()
    private val willCostLabel: Label by fxid()
    private val perTextField: TextField by fxid()
    private val perCostLabel: Label by fxid()
    private val fpTextField: TextField by fxid()
    private val fpCostLabel: Label by fxid()

    private val bsTextField: TextField by fxid()
    private val bsCostLabel: Label by fxid()

    private val moveTextField: TextField by fxid()
    private val moveCostLabel: Label by fxid()

    private val dogeLabel: Label by fxid()
    private val blLabel: Label by fxid()
    private val thrustLabel: Label by fxid()
    private val swingLabel: Label by fxid()

    private val blockLabel: Label by fxid()
    private val parryLabel: Label by fxid()

    private val languagesTableView: TableView<Language> by fxid()
    private val languagesHeadColumn: TableColumn<Any, Any> by fxid()
    private val languageNameColumn: TableColumn<Language, String> by fxid()
    private val spokenColumn: TableColumn<Language, String> by fxid()
    private val writtenColumn: TableColumn<Language, String> by fxid()

    private val culturesTableView: TableView<Cultura> by fxid()
    private val culturesNameColumn: TableColumn<Cultura, String> by fxid()

    private val advantagesTableView: TableView<Feature> by fxid()
    private val advantagesHeadColumn: TableColumn<Any, Any> by fxid()
    private val advantagesNameColumn: TableColumn<Feature, String> by fxid()

    private val disadvantagesTableView: TableView<Feature> by fxid()
    private val disadvantagesHeadColumn: TableColumn<Any, Any> by fxid()
    private val disadvantagesNameColumn: TableColumn<Feature, String> by fxid()

    private val skillsTableView: TableView<Skill> by fxid()
    private val skillsHeadColumn: TableColumn<Any, Any> by fxid()
    private val skillsNameColumn: TableColumn<Skill, String> by fxid()
    private val skillsTypeColumn: TableColumn<Skill, String> by fxid()
    private val skillsLevelColumn: TableColumn<Skill, Int> by fxid()

    private val spellsTableView: TableView<Spell> by fxid()
    private val spellsHeadColumn: TableColumn<Any, Any> by fxid()
    private val spellsNameColumn: TableColumn<Spell, String> by fxid()
    private val spellsComplexityColumn: TableColumn<Spell, String> by fxid()
    private val spellsLevelColumn: TableColumn<Spell, Int> by fxid()

    private val techniquesTableView: TableView<Technique> by fxid()
    private val techniquesHeadColumn: TableColumn<Any, Any> by fxid()
    private val techniquesName: TableColumn<Technique, String> by fxid()
    private val techniquesTypeColumn: TableColumn<Technique, String> by fxid()
    private val techniquesLevelColumn: TableColumn<Technique, Int> by fxid()

    private val alchemiesTableView: TableView<Alchemy> by fxid()
    private val alchemiesHeadColumn: TableColumn<Any, Any> by fxid()
    private val alchemiesNameColumn: TableColumn<Alchemy, String> by fxid()

    init {
        setInfoCallbacks()
        setAttributesCallbacks()
        tablesCallbacks()
        setColumns()
    }

    override fun onDock() {
        setInfo()
        setAttributes()
        setTables()
    }

    private fun setInfo() {
        nameTextField.text = character.name
        playerTextField.text = character.player
        growthTextField.text = character.growth.toString()
        weightTextField.text = character.weight.toString()
        ageTextField.text = character.age.toString()
        maxPointsTextField.text = character.maxPoints.toString()
        currentPointsLabel.text = character.currentPoints.toString()
        blockLabel.text = CharacterParams.block()
        parryLabel.text = CharacterParams.parry()

        pointsColor()
    }

    private fun setAttributes() {
        smTextField.text = character.sm.toString()
        noFineManipulatorsCheckBox.isSelected = character.noFineManipulators
        tlTextField.text = character.tl.toString()
        tlCostTextField.text = character.tlCost.toString()
        stTextField.text = character.st.toString()
        dxTextField.text = character.dx.toString()
        iqTextField.text = character.iq.toString()
        htTextField.text = character.ht.toString()
        hpTextField.text = character.hp.toString()
        willTextField.text = character.will.toString()
        perTextField.text = character.per.toString()
        fpTextField.text = character.fp.toString()
        bsTextField.text = character.bs.toString()
        moveTextField.text = character.move.toString()
    }

    private fun setTables() {
        setLanguages()
        setCultures()
        setAdvantages()
        setDisadvantages()
        setSkills()
        setSpells()
        setTechniques()
        setAlchemies()
    }

    private fun setLanguages() {
        val languages = character.languages()
        var spent_points = 0
        languages.forEach { spent_points += it.cost }
        languagesTableView.items = languages
        languagesHeadColumn.text = "${messages["languages"]} | ${messages["spent_points"]}: $spent_points"
    }

    private fun setCultures() {
        val cultures = character.cultures()
        var spent_points = 0
        cultures.forEach { spent_points += it.cost }
        culturesTableView.items = cultures
        culturesNameColumn.text = "${messages["cultures"]} | ${messages["spent_points"]}: $spent_points"
    }

    private fun setAdvantages() {
        val params = HashMap<String, Any>()
        params["advantage"] = true
        val advantages = character.features(params)
        var spent_points = 0
        advantages.forEach { spent_points += it.cost }
        advantagesTableView.items = advantages
        advantagesHeadColumn.text = "${messages["advantages_and_perks"]} | ${messages["spent_points"]}: $spent_points"
    }

    private fun setDisadvantages() {
        val params = HashMap<String, Any>()
        params["advantage"] = false
        val disadvantages = character.features(params)
        var spent_points = 0
        disadvantages.forEach { spent_points += it.cost }
        disadvantagesTableView.items = disadvantages
        disadvantagesHeadColumn.text = "${messages["disadvantages_and_quirk"]} | ${messages["spent_points"]}: $spent_points"
    }

    private fun setSkills() {
        val skills = character.skills()
        var spent_points = 0
        skills.forEach { spent_points += it.cost }
        skillsTableView.items = skills
        skillsHeadColumn.text = "${messages["skills"]} | ${messages["spent_points"]}: $spent_points"
    }

    private fun setSpells() {
        val spells = character.spells()
        var spent_points = 0
        spells.forEach { spent_points += it.finalCost }
        spellsTableView.items = spells
        spellsHeadColumn.text = "${messages["spells"]} | ${messages["spent_points"]}: $spent_points"
    }

    private fun setTechniques() {
        val techniques = character.techniques()
        var spent_points = 0
        techniques.forEach { spent_points += it.cost }
        techniquesTableView.items = techniques
        techniquesHeadColumn.text = "${messages["techniques"]} | ${messages["spent_points"]}: $spent_points"
    }

    private fun setAlchemies() {
        val alchemies = character.alchemies()
        alchemiesTableView.items = alchemies
        alchemiesHeadColumn.text = messages["alchemies"]
    }

    private fun pointsColor() {
        if (character.maxPoints >= character.currentPoints) currentPointsLabel.textFill = Color.GREEN
        else currentPointsLabel.textFill = Color.RED
    }

    private fun setInfoCallbacks(){
        nameTextField.textProperty().addListener { _, _, value ->
            if(value.isNullOrBlank() || character.name == value)return@addListener
            paramsView.name.text = value
        }

        playerTextField.textProperty().addListener { _, _, value ->
            if(value.isNullOrBlank() || character.player == value)return@addListener
            paramsView.player.text = value
        }

        growthTextField.textProperty().addListener { _, _, value ->
            if(!Regex("\\d+").matches(value) || character.growth.toString() == value)return@addListener
            paramsView.growth.text = value
        }

        weightTextField.textProperty().addListener { _, _, value ->
            if(!Regex("\\d+").matches(value) || character.weight.toString() == value)return@addListener
            paramsView.weight.text = value
        }

        ageTextField.textProperty().addListener { _, _, value ->
            if(!Regex("\\d+").matches(value) || character.age.toString() == value)return@addListener
            paramsView.age.text = value
        }

        maxPointsTextField.textProperty().addListener { _, _, value ->
            if(!Regex("\\d+").matches(value) || character.maxPoints.toString() == value)return@addListener
            tabsView.maxPoints.text = value
            pointsColor()
        }
    }

    private fun setAttributesCallbacks() {
        smTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeSm(value, stCostLabel, hpCostLabel))
            paramsView.sm.text = value
        }

        noFineManipulatorsCheckBox.selectedProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeNoFineManipulators(value, stCostLabel, dxCostLabel))
            paramsView.noFineManipulators.isSelected = value
        }


        stTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeSt(value, stCostLabel, hpCostLabel, blLabel, thrustLabel, swingLabel))
            paramsView.st.text = value
        }

        dxTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeDx(value, dxCostLabel, bsCostLabel, moveCostLabel, dogeLabel))
            paramsView.dx.text = value
        }

        iqTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeIq(value, iqCostLabel, willCostLabel, perCostLabel))
            paramsView.iq.text = value
        }

        htTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeHt(value, htCostLabel, fpCostLabel, bsCostLabel, moveCostLabel))
            paramsView.ht.text = value
        }

        hpTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeHp(value, hpCostLabel))
            paramsView.hp.text = value
        }

        willTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeWill(value, willCostLabel))
            paramsView.will.text = value
        }

        perTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changePer(value, perCostLabel))
            paramsView.per.text = value
        }

        fpTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeFp(value, fpCostLabel))
            paramsView.fp.text = value
        }

        bsTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeBs(value, bsCostLabel, moveCostLabel, dogeLabel))
            paramsView.bs.text = value
        }

        moveTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeMove(value, moveCostLabel))
            paramsView.move.text = value
        }

        tlTextField.textProperty().addListener { _, _, value ->
            CharacterChange.changeTl(value)
            paramsView.tl.text = value
        }

        tlCostTextField.textProperty().addListener { _, _, value ->
            currentPoints(CharacterChange.changeTlCost(value))
            paramsView.tlCost.text = value
        }
    }

    private fun setColumns() {
        languageNameColumn.cellValueFactory = PropertyValueFactory<Language, String>("name")
        spokenColumn.cellValueFactory = PropertyValueFactory<Language, String>("spoken")
        writtenColumn.cellValueFactory = PropertyValueFactory<Language, String>("written")
        culturesNameColumn.cellValueFactory = PropertyValueFactory<Cultura, String>("name")
        advantagesNameColumn.cellValueFactory = PropertyValueFactory<Feature, String>("name")
        disadvantagesNameColumn.cellValueFactory = PropertyValueFactory<Feature, String>("name")
        skillsNameColumn.cellValueFactory = PropertyValueFactory<Skill, String>("name")
        skillsTypeColumn.cellValueFactory = PropertyValueFactory<Skill, String>("typeAndComplexity")
        skillsLevelColumn.cellValueFactory = PropertyValueFactory<Skill, Int>("level")
        spellsNameColumn.cellValueFactory = PropertyValueFactory<Spell, String>("name")
        spellsComplexityColumn.cellValueFactory = PropertyValueFactory<Spell, String>("complexity")
        spellsLevelColumn.cellValueFactory = PropertyValueFactory<Spell, Int>("level")
        techniquesName.cellValueFactory = PropertyValueFactory<Technique, String>("name")
        techniquesTypeColumn.cellValueFactory = PropertyValueFactory<Technique, String>("typeAndComplexity")
        techniquesLevelColumn.cellValueFactory = PropertyValueFactory<Technique, Int>("level")
        alchemiesNameColumn.cellValueFactory = PropertyValueFactory<Alchemy, String>("name")
    }

    private fun tablesCallbacks() {

    }

    private fun currentPoints(cost: Int) {
        val currentPoints = character.currentPoints + cost
        currentPointsLabel.text = currentPoints.toString()
        pointsColor()
        tabsView.setCurrentPoints(currentPoints)
    }

    private fun <M: Model> isLeftClickTable(table: TableView<M>, event: MouseEvent): Boolean {
        return event.button == MouseButton.PRIMARY && (table.items.isEmpty() || event.clickCount == 2)
    }
}
