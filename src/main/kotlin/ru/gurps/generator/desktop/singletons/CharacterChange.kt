package ru.gurps.generator.desktop.singletons

import javafx.scene.control.Label

object CharacterChange {
    init { println("This Character change is load") }
    var character = Characters.current!!

    fun changeSm(value: String, stCost: Label, hpCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.sm.toString() != value) {
            val intValue = value.toInt()
            character.sm = intValue
        }

        val oldStCost = stCost.text.toInt()
        val oldHpCost = hpCost.text.toInt()
        val newStCost = CharacterParams.stCost()
        val newHpCost = CharacterParams.hpCost()

        stCost.text = newStCost.toString()
        hpCost.text = newHpCost.toString()

        return (newStCost - oldStCost) + (newHpCost - oldHpCost)
    }

    fun changeNoFineManipulators(value: Boolean, stCost: Label, dxCost: Label): Int {
        if(character.noFineManipulators == value) return 0
        character.noFineManipulators = value
        val oldStCost = stCost.text.toInt()
        val oldDxCost = dxCost.text.toInt()

        val newStCost = stCost.text.toInt()
        val newDxCost = dxCost.text.toInt()

        stCost.text = newStCost.toString()
        dxCost.text = newDxCost.toString()

        return (newStCost - oldStCost) + (newDxCost - oldDxCost)
    }

    fun changeSt(value: String, stCost: Label, hpCost: Label, bl: Label, thrust: Label, swing: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.st.toString() != value) character.st = value.toInt()

        val oldStCost = stCost.text.toInt()
        val oldHpCost = hpCost.text.toInt()
        val newStCost = CharacterParams.stCost()
        val newHpCost = CharacterParams.hpCost()

        stCost.text = newStCost.toString()
        hpCost.text = newHpCost.toString()
        bl.text = CharacterParams.bg().toString()
        thrust.text = Dmg.thrust(character.st)
        swing.text = Dmg.swing(character.st)

        return (newStCost - oldStCost) + (newHpCost - oldHpCost)
    }

    fun changeDx(value: String, dxCost: Label, bsCost: Label, moveCost: Label, doge: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.dx.toString() != value) character.dx = value.toInt()
        val oldDxCost = dxCost.text.toInt()
        val oldBsCost = bsCost.text.toInt()
        val oldMoveCost = moveCost.text.toInt()

        val newDxCost = CharacterParams.dxCost()
        val newBsCost = CharacterParams.bsCost()
        val newMoveCost = CharacterParams.moveCost()

        dxCost.text = newDxCost.toString()
        bsCost.text = newBsCost.toString()
        moveCost.text = CharacterParams.moveCost().toString()
        doge.text = newMoveCost.toString()

        return (newDxCost - oldDxCost) + (newBsCost - oldBsCost) + (newMoveCost - oldMoveCost)
    }

    fun changeIq(value: String, iqCost: Label, willCost: Label, perCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.iq.toString() != value) character.iq = value.toInt()
        val oldIqCost = iqCost.text.toInt()
        val oldWillCost = willCost.text.toInt()
        val oldPerCost = perCost.text.toInt()

        val newIqCost = CharacterParams.iqCost()
        val newWillCost = CharacterParams.willCost()
        val newPerCost = CharacterParams.perCost()

        iqCost.text = newIqCost.toString()
        willCost.text = newWillCost.toString()
        perCost.text = newPerCost.toString()

        return (newIqCost - oldIqCost) + (newWillCost - oldWillCost) + (newPerCost - oldPerCost)
    }

    fun changeHt(value: String, htCost: Label, fpCost: Label, bsCost: Label, moveCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.ht.toString() != value) character.ht = value.toInt()
        val oldHtCost = htCost.text.toInt()
        val oldFpCost = fpCost.text.toInt()
        val oldBsCost = bsCost.text.toInt()
        val oldMoveCost = moveCost.text.toInt()

        val newHtCost = CharacterParams.htCost()
        val newFpCost = CharacterParams.fpCost()
        val newBsCost = CharacterParams.bsCost()
        val newMoveCost = CharacterParams.moveCost()

        htCost.text = newHtCost.toString()
        fpCost.text = newFpCost.toString()
        bsCost.text = newBsCost.toString()
        moveCost.text = newMoveCost.toString()

        return (newHtCost - oldHtCost) + (newFpCost - oldFpCost) + (newBsCost - oldBsCost) + (newMoveCost - oldMoveCost)
    }

    fun changeHp(value: String, hpCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.hp.toString() != value) character.hp = value.toInt()
        val oldHpCost = hpCost.text.toInt()
        val newHpCost = CharacterParams.hpCost()

        hpCost.text = newHpCost.toString()

        return newHpCost - oldHpCost
    }

    fun changeWill(value: String, willCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.will.toString() != value) character.will = value.toInt()
        val oldWillCost = willCost.text.toInt()
        val newWillCost = CharacterParams.willCost()

        willCost.text = newWillCost.toString()

        return newWillCost - oldWillCost
    }

    fun changePer(value: String, perCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.per.toString() != value) character.per = value.toInt()
        val oldPerCost = perCost.text.toInt()
        val newPerCost = CharacterParams.perCost()

        perCost.text = newPerCost.toString()

        return newPerCost - oldPerCost
    }

    fun changeFp(value: String, fpCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.fp.toString() != value) character.fp = value.toInt()
        val oldFpCost = fpCost.text.toInt()
        val newFpCost = CharacterParams.fpCost()

        fpCost.text = newFpCost.toString()

        return newFpCost - oldFpCost
    }

    fun changeBs(value: String, bsCost: Label, moveCost: Label, doge: Label): Int {
        if(!Regex("[0-9]{1,13}(\\.[0-9]*)?").matches(value)) return 0
        if(character.bs.toString() != value) character.bs = value.toDouble()
        val oldBsCost = bsCost.text.toInt()
        val oldMoveCost = moveCost.text.toInt()
        val newBsCost = CharacterParams.bsCost()
        val newMoveCost = CharacterParams.moveCost()

        bsCost.text = newBsCost.toString()
        moveCost.text = newMoveCost.toString()
        doge.text = CharacterParams.doge().toString()

        return (newBsCost - oldBsCost) + (newMoveCost - oldMoveCost)
    }

    fun changeMove(value: String, moveCost: Label): Int {
        if(!Regex("\\d+").matches(value)) return 0
        if(character.move.toString() != value) character.move = value.toInt()
        val oldMoveCost = moveCost.text.toInt()
        val newMoveCost = CharacterParams.moveCost()

        moveCost.text = newMoveCost.toString()
        return newMoveCost - oldMoveCost
    }

    fun changeTl(value: String) {
        if(!Regex("\\d+").matches(value) || character.tl.toString() != value) return
        character.tl = value.toInt()
        character.save()
    }

    fun changeTlCost(value: String): Int {
        if(!Regex("(-?[0-9]+)").matches(value)) return 0
        val oldTlCost = character.tlCost
        val newTlCost = value.toInt()

        if(oldTlCost == newTlCost) return 0
        character.tlCost = newTlCost
        return newTlCost - oldTlCost
    }
}