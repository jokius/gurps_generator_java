package ru.gurps.generator.desktop.views.characters

import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import ru.gurps.generator.desktop.singletons.CharacterChange
import ru.gurps.generator.desktop.singletons.Dmg
import ru.gurps.generator.desktop.singletons.CharacterParams
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.View

class ParamsView : View() {
    override val root: AnchorPane by fxml("/templates/characters/ParamsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    val sm: TextField by fxid()
    val noFineManipulators: CheckBox by fxid()
    val st: TextField by fxid()
    private val stCost: Label by fxid()
    val dx: TextField by fxid()
    private val dxCost: Label by fxid()
    val iq: TextField by fxid()
    private val iqCost: Label by fxid()
    val ht: TextField by fxid()
    private val htCost: Label by fxid()
    val hp: TextField by fxid()
    private val hpCost: Label by fxid()
    val will: TextField by fxid()
    private val willCost: Label by fxid()
    val per: TextField by fxid()
    private val perCost: Label by fxid()
    val fp: TextField by fxid()
    private val fpCost: Label by fxid()
    val bs: TextField by fxid()
    private val bsCost: Label by fxid()
    private val bg: Label by fxid()
    val move: TextField by fxid()
    private val moveCost: Label by fxid()
    private val doge: Label by fxid()
    private val thrust: Label by fxid()
    private val swing: Label by fxid()
    val name: TextField by fxid()
    val player: TextField by fxid()
    val growth: TextField by fxid()
    val weight: TextField by fxid()
    val age: TextField by fxid()
    val tl: TextField by fxid()
    val tlCost: TextField by fxid()

    init {
        sm.text = Integer.toString(character.sm)
        noFineManipulators.isSelected = character.noFineManipulators

        st.text = Integer.toString(character.st)
        dx.text = Integer.toString(character.dx)
        iq.text = Integer.toString(character.iq)
        ht.text = Integer.toString(character.ht)

        hp.text = Integer.toString(character.hp)
        will.text = Integer.toString(character.will)
        per.text = Integer.toString(character.per)
        fp.text = Integer.toString(character.fp)

        bs.text = java.lang.Double.toString(character.bs)
        move.text = Integer.toString(character.move)

        name.text = character.name
        player.text = character.player
        growth.text = Integer.toString(character.growth)
        weight.text = Integer.toString(character.weight)
        age.text = Integer.toString(character.age)

        tl.text = Integer.toString(character.tl)
        tlCost.text = Integer.toString(character.tlCost)

        stCost.text = Integer.toString(CharacterParams.stCost())
        dxCost.text = Integer.toString(CharacterParams.dxCost())
        iqCost.text = Integer.toString(CharacterParams.iqCost())
        htCost.text = Integer.toString(CharacterParams.htCost())

        hpCost.text = Integer.toString(CharacterParams.hpCost())
        willCost.text = Integer.toString(CharacterParams.willCost())
        perCost.text = Integer.toString(CharacterParams.perCost())
        fpCost.text = Integer.toString(CharacterParams.fpCost())

        bsCost.text = Integer.toString(CharacterParams.bsCost())
        moveCost.text = Integer.toString(CharacterParams.moveCost())

        bg.text = Integer.toString(CharacterParams.bg())
        doge.text = Integer.toString(CharacterParams.doge())

        thrust.text = Dmg.thrust(character.st)
        swing.text = Dmg.swing(character.st)

        textEvents()
    }

    private fun textEvents() {
        sm.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changeSm(value, stCost, hpCost))
        }

        noFineManipulators.selectedProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints +
                    CharacterChange.changeNoFineManipulators(value, stCost, dxCost))
        }

        st.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints +
                    CharacterChange.changeSt(value, stCost, hpCost, bg, thrust, swing))
        }

        dx.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints +
                    CharacterChange.changeDx(value, dxCost, bsCost, moveCost, doge))
        }

        iq.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints +
                    CharacterChange.changeIq(value, iqCost, willCost, perCost))
        }

        ht.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints +
                    CharacterChange.changeHt(value, htCost, fpCost, bsCost, moveCost))
        }

        hp.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changeHp(value, hpCost))
        }

        will.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changeWill(value, willCost))
        }

        per.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changePer(value, perCost))
        }

        fp.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changeFp(value, fpCost))
        }

        bs.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changeBs(value, bsCost, moveCost, doge))
        }

        move.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changeMove(value, moveCost))
        }

        tl.textProperty().addListener { _, _, value ->
            CharacterChange.changeTl(value)
        }

        tlCost.textProperty().addListener { _, _, value ->
            tabsView.setCurrentPoints(character.currentPoints + CharacterChange.changeTlCost(value))
        }

        growth.textProperty().addListener { _, _, value ->
            if (value.isNullOrBlank() || character.growth.toString() == value) return@addListener
            character.growth = value.toInt()
            character.save()
        }

        weight.textProperty().addListener { _, _, value ->
            if (value.isNullOrBlank() || character.weight.toString() == value) return@addListener
            character.weight = value.toInt()
            character.save()
        }

        age.textProperty().addListener { _, _, value ->
            if (value.isNullOrBlank() || character.age.toString() == value) return@addListener
            character.age = value.toInt()
            character.save()
        }

        name.textProperty().addListener { _, _, value ->
            if (character.name == value || character.name == value) return@addListener
            character.name = value
            character.save()
        }

        player.textProperty().addListener { _, _, value ->
            if (character.player == value || character.player == value) return@addListener
            character.player = value
            character.save()
        }
    }
}
