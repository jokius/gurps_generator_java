package ru.gurps.generator.desktop.views

import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.Property
import ru.gurps.generator.desktop.views.characters.ParamsView
import ru.gurps.generator.desktop.views.characters.SelectView
import ru.gurps.generator.desktop.views.characters.SheetView
import ru.gurps.generator.desktop.views.tables.*
import tornadofx.*

class TabsView : View() {
    override val root: AnchorPane by fxml("/templates/TabsView.fxml")
    val character = Characters.current!!
    private val selectView: SelectView by inject()
    private val characterParamsView: ParamsView by inject()
    private val featuresView: FeaturesView by inject()
    private val quirksView: QuirksView by inject()
    private val modifiersView: ModifiersView by inject()
    private val skillsView: SkillsView by inject()
    private val techniqueView: TechniquesView by inject()
    private val spellsView: SpellsView by inject()
    private val alchemiesView: AlchemiesView by inject()
    private val languagesView: LanguagesView by inject()
    private val culturesView: CulturesView by inject()
    private val equipmentsView: EquipmentsView by inject()
    private val meleeWeaponsView: MeleeWeaponsView by inject()
    private val grenadesView: GrenadesView by inject()
    private val gunsView: GunsView by inject()
    private val armorsView: ArmorsView by inject()
    private val shieldsView: ShieldsView by inject()
    private val transporsView: TransportsView by inject()

    val maxPoints: TextField by fxid()
    val currentPoints: Label by fxid()
    private val moneyTotalDollars: TextField by fxid()
    private val moneyLeftDollars: Label by fxid()
    private val characterSheet: Button by fxid()

    // menus
    private val viewMenu: Menu by fxid()
    private val newMenuItem: MenuItem by fxid()

    // tabs
    private val mainTabPanel: TabPane by fxid()

    private val paramsTab: Tab by fxid()
    private val featuresTab: Tab by fxid()
    private val quirksTab: Tab by fxid()
    private val modifiersTab: Tab by fxid()
    private val skillsTab: Tab by fxid()
    private val techniquesTab: Tab by fxid()
    private val spellsTab: Tab by fxid()
    private val alchemiesTab: Tab by fxid()
    private val languagesTab: Tab by fxid()
    private val culturesTab: Tab by fxid()
    private val equipmentsTab: Tab by fxid()
    private val meleeWeaponsTab: Tab by fxid()
    private val grenadesTab: Tab by fxid()
    private val gunsTab: Tab by fxid()
    private val armorsTab: Tab by fxid()
    private val shieldsTab: Tab by fxid()
    private val transporsTab: Tab by fxid()

    init {
        title = "GURPS Generator"
        menus()
        tabsConfigure()
        pointsColor()
        moneyColor()
        maxPoints.text = character.maxPoints.toString()
        currentPoints.text = character.currentPoints.toString()
        maxPoints.textProperty().addListener { _, _, value ->
            if (value.isNullOrBlank()) return@addListener
            if (!value.matches("\\d+".toRegex())) {
                maxPoints.text = character.maxPoints.toString()
                return@addListener
            }

            if (character.maxPoints == value.toInt()) return@addListener
            character.maxPoints = value.toInt()
            character.save()
            maxPoints.text = value
            pointsColor()
        }

        moneyTotalDollars.text = character.moneyTotalDollars.toString()
        moneyLeftDollars.text = character.moneyLeft
        moneyTotalDollars.textProperty().addListener { _, _, value ->
            if (value.isNullOrBlank()) return@addListener
            if (!value.matches("\\d+".toRegex())) {
                moneyTotalDollars.text = character.moneyTotalDollars.toString()
                return@addListener
            }

            if (character.moneyTotalDollars == value.toLong()) return@addListener
            character.moneyTotalDollars = value.toLong()
            character.save()
            moneyTotalDollars.text = value
            moneyLeftDollars.text = character.moneyLeft
            moneyColor()
        }

        characterSheet.setOnAction {
            characterSheet()
        }
    }

    private fun pointsColor() {
        if (character.maxPoints >= character.currentPoints) currentPoints.textFill = Color.GREEN
        else currentPoints.textFill = Color.RED
    }

    private fun moneyColor() {
        if (character.moneyTotal >= character.inventoryCost) moneyLeftDollars.textFill = Color.GREEN
        else moneyLeftDollars.textFill = Color.RED
    }

    override fun onDock() {
        primaryStage.isResizable = true
        primaryStage.isMaximized = true
        Property.selectView = selectView.selectViewChoiceBox.value
        if (selectView.selectViewChoiceBox.value == messages["character_sheet"]) characterSheet()
    }

    private fun menus() {
        newMenuItem.setOnAction {
            Characters.current = Character()
            selectView.reload = true
            replaceWith(SelectView::class, ViewTransition.Slide(0.001.seconds))
        }
    }

    private fun tabsConfigure() {
        paramsTab.content = characterParamsView.root
        featuresTab.content = featuresView.root
        quirksTab.content = quirksView.root
        modifiersTab.content = modifiersView.root
        skillsTab.content = skillsView.root
        techniquesTab.content = techniqueView.root
        spellsTab.content = spellsView.root
        alchemiesTab.content = alchemiesView.root
        languagesTab.content = languagesView.root
        culturesTab.content = culturesView.root
        equipmentsTab.content = equipmentsView.root
        meleeWeaponsTab.content = meleeWeaponsView.root
        grenadesTab.content = grenadesView.root
        gunsTab.content = gunsView.root
        armorsTab.content = armorsView.root
        shieldsTab.content = shieldsView.root
        transporsTab.content = transporsView.root
        val checkMenuItems = arrayOf(paramsTab, quirksTab, modifiersTab, skillsTab,
                techniquesTab, spellsTab, alchemiesTab, languagesTab, culturesTab, equipmentsTab, meleeWeaponsTab,
                grenadesTab, gunsTab, armorsTab, shieldsTab, transporsTab)
        val items = FXCollections.observableArrayList<CheckMenuItem>()
        checkMenuItems.forEach {
            val checkMenuItem = CheckMenuItem(it.text)
            checkMenuItem.isSelected = true
            it.setOnClosed { checkMenuItem.isSelected = false }
            checkMenuItem.selectedProperty().addListener { _, _, value ->
                if (value)
                    mainTabPanel.tabs.add(it)
                else
                    mainTabPanel.tabs.remove(it)
            }

            items.add(checkMenuItem)
        }

        setLoadData()
        viewMenu.items.addAll(items)
    }

    private fun setLoadData() {
        featuresTab.setOnSelectionChanged {
            featuresView.lazyLoad
        }

        quirksTab.setOnSelectionChanged {
            quirksView.lazyLoad
        }

        modifiersTab.setOnSelectionChanged {
            modifiersView.lazyLoad
        }

        skillsTab.setOnSelectionChanged {
            skillsView.lazyLoad
        }

        techniquesTab.setOnSelectionChanged {
            techniqueView.lazyLoad
        }

        spellsTab.setOnSelectionChanged {
            spellsView.lazyLoad
        }

        alchemiesTab.setOnSelectionChanged {
            alchemiesView.lazyLoad
        }

        languagesTab.setOnSelectionChanged {
            languagesView.lazyLoad
        }

        culturesTab.setOnSelectionChanged {
            culturesView.lazyLoad
        }

        equipmentsTab.setOnSelectionChanged {
            equipmentsView.lazyLoad
        }

        meleeWeaponsTab.setOnSelectionChanged {
            meleeWeaponsView.lazyLoad
        }

        grenadesTab.setOnSelectionChanged {
            grenadesView.lazyLoad
        }

        gunsTab.setOnSelectionChanged {
            gunsView.lazyLoad
        }

        armorsTab.setOnSelectionChanged {
            armorsView.lazyLoad
        }

        shieldsTab.setOnSelectionChanged {
            shieldsView.lazyLoad
        }

        transporsTab.setOnSelectionChanged {
            transporsView.lazyLoad
        }
    }

    fun setCurrentPoints(points: Int) {
        currentPoints.text = points.toString()
        character.currentPoints = points
        character.save()
        pointsColor()
    }

    fun setInventoryCost(cost: Long) {
        character.inventoryCost += cost
        moneyLeftDollars.text = character.moneyLeft
        character.save()
        moneyColor()
    }

    private fun characterSheet() {
        find(SheetView::class).openWindow(resizable = false)
    }
}