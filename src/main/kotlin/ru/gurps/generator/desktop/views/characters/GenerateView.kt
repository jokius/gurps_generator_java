package ru.gurps.generator.desktop.views.characters

import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.MenuButton
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import ru.gurps.generator.desktop.models.characters.*
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsAir
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsGround
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsSpace
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsWater
import ru.gurps.generator.desktop.models.rules.*
import ru.gurps.generator.desktop.models.rules.transports.TransportsAir
import ru.gurps.generator.desktop.models.rules.transports.TransportsGround
import ru.gurps.generator.desktop.models.rules.transports.TransportsSpace
import ru.gurps.generator.desktop.models.rules.transports.TransportsWater
import ru.gurps.generator.desktop.singletons.*
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class GenerateView : View() {
    override val root: AnchorPane by fxml("/templates/characters/GenerateView.fxml")
    var character = Characters.current!!
    private val selectView: SelectView by inject()

    private val name: TextField by fxid()
    private val characteristics: TextField by fxid()
    private val tl: TextField by fxid()
    private val advantages: TextField by fxid()
    private val disadvantages: TextField by fxid()
    private val skills: TextField by fxid()
    private val techniques: TextField by fxid()
    private val spells: TextField by fxid()
    private val equipmentsCost: TextField by fxid()
    private val manArmorCost: TextField by fxid()
    private val notNamArmorCost: TextField by fxid()
    private val meleeWeaponCost: TextField by fxid()
    private val meleeWeaponRangedCost: TextField by fxid()
    private val shieldsCost: TextField by fxid()
    private val gunsCost: TextField by fxid()
    private val grenadesCost: TextField by fxid()
    private val transportAirCost: TextField by fxid()
    private val transportGroundCost: TextField by fxid()
    private val transportWaterCost: TextField by fxid()
    private val transportSpaceCost: TextField by fxid()
    private val alchemiesCost: TextField by fxid()

    private val advantagesMb: MenuButton by fxid()
    private val disadvantagesMb: MenuButton by fxid()
    private val skillsCharacteristicsMb: MenuButton by fxid()
    private val skillsComplexitiesMb: MenuButton by fxid()
    private val techniquesMb: MenuButton by fxid()
    private val spellsTypesMb: MenuButton by fxid()
    private val spellsSchoolsMb: MenuButton by fxid()

    private val advantagesH: HashMap<String, Int> = HashMap()
    private val disadvantagesH: HashMap<String, Int> = HashMap()
    private val skillsCharacteristicsH: HashMap<String, Int> = HashMap()
    private val skillsComplexitiesH: HashMap<String, Int> = HashMap()
    private val techniquesH: HashMap<String, Int> = HashMap()
    private val spellsTypesH: HashMap<String, Int> = HashMap()
    private val spellsSchoolsH: HashMap<String, Int> = HashMap()

    private val generateButton: Button by  fxid()

    init {
        title = messages["app_name_character_generate"]
        stageParams()
        for (textField in textFields()) setTextFieldParams(textField)
        setCheckBoxes()
    }

    override fun onDock() {
        name.text = ""
        stageParams()
    }

    private fun stageParams() {
        primaryStage.sizeToScene()
    }

    fun back() {
        Characters.current = null
        selectView.reload = true
        replaceWith(SelectView::class, ViewTransition.Slide(0.001.seconds))
    }

    fun generate() {
        character.name = name.text
        character.save()
        var cost = 0
        for (textField in textFields()) {
            if (textField.id == "name" || textField.text == "") continue
            val points = Math.abs(textField.text.toInt())
            if (points == 0) continue
            cost += points
            when(textField.id) {
                "characteristics" -> characteristicsGenerate(points)
                "tl" -> tlSet(points)
                "advantages" -> advantagesGenerate(points)
                "disadvantages" -> disadvantagesGenerate(points)
                "skills" -> skillsGenerate(points)
                "techniques" -> techniquesGenerate(points)
                "spells" -> spellsGenerate(points)
                "equipmentsCost" -> equipmentsGenerate(points)
                "manArmorCost" -> manArmorGenerate(points)
                "notNamArmorCost" -> notNamArmorGenerate(points)
                "meleeWeaponCost" -> meleeWeaponGenerate(points)
                "meleeWeaponRangedCost" -> meleeWeaponRangedGenerate(points)
                "shieldsCost" -> shieldsGenerate(points)
                "gunsCost" -> gunsGenerate(points)
                "grenadesCost" -> grenadesGenerate(points)
                "transportAirCost" -> transportAirGenerate(points)
                "transportGroundCost" -> transportGroundGenerate(points)
                "transportWaterCost" -> transportWaterGenerate(points)
                "transportSpaceCost" -> transportSpaceGenerate(points)
                "alchemiesCost" -> alchemiesGenerate(points)
            }
        }

        character.maxPoints = cost
        character.save()
        replaceWith(TabsView::class, ViewTransition.Slide(0.001.seconds))
    }

    private fun textFields(): Array<TextField> {
        return arrayOf(name, characteristics, tl, advantages, disadvantages, skills, techniques, spells, equipmentsCost,
                manArmorCost, notNamArmorCost, meleeWeaponCost, meleeWeaponRangedCost, shieldsCost, gunsCost,
                grenadesCost, transportAirCost, transportGroundCost, transportWaterCost, transportSpaceCost,
                alchemiesCost)
    }

    private fun setTextFieldParams(textField: TextField) {
        textField.textProperty().addListener { _, _, new ->
            if (new == "") {
                generateButton.isDisable = true
                return@addListener
            }
            if (name.text == "") {
                generateButton.isDisable = true
                return@addListener
            }
            generateButton.isDisable = false
        }
    }

    private fun setCheckBoxes() {
        setAdvantagesCm()
        setDisadvantagesCm()
        setSkillsCm()
        setTechniquesCm()
        setSpellsCm()
    }

    private fun setAdvantagesCm(){
        for((i, name) in FeatureTypes.list.withIndex()) {
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { observableValue, arg1, arg2 ->
                if (observableValue.value){
                    advantagesH[name] = i
                } else {
                    advantagesH.remove(name)
                }
             }

            advantagesH[name] = i
            advantagesMb.items.add(cm)
        }
    }

    private fun setDisadvantagesCm(){
        for((i, name) in FeatureTypes.list.withIndex()) {
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { observableValue, _, _ ->
                if (observableValue.value){
                    disadvantagesH[name] = i
                } else {
                    disadvantagesH.remove(name)
                }
            }

            disadvantagesH[name] = i
            disadvantagesMb.items.add(cm)
        }
    }

    private fun setSkillsCm(){
        for((i, name) in CharacteristicTypes.list.withIndex()) {
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { observableValue, _, _ ->
                if (observableValue.value){
                    skillsCharacteristicsH[name] = i
                } else {
                    skillsCharacteristicsH.remove(name)
                }
            }

            skillsCharacteristicsH[name] = i
            skillsCharacteristicsMb.items.add(cm)
        }

        for((i, name) in ComplexityTypes.list.withIndex()) {
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { observableValue, _, _ ->
                if (observableValue.value){
                    skillsComplexitiesH[name] = i
                } else {
                    skillsComplexitiesH.remove(name)
                }
            }

            skillsComplexitiesH[name] = i
            skillsComplexitiesMb.items.add(cm)
        }
    }

    private fun setTechniquesCm(){
        for((i, name) in ComplexityTypes.list.withIndex()) {
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { observableValue, _, _ ->
                if (observableValue.value){
                    techniquesH[name] = i
                } else {
                    techniquesH.remove(name)
                }
            }

            techniquesH[name] = i
            techniquesMb.items.add(cm)
        }
    }

    private fun setSpellsCm(){
        for((i, name) in SpellType.list.withIndex()) {
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { observableValue, _, _ ->
                if (observableValue.value){
                    spellsTypesH[name] = i
                } else {
                    spellsTypesH.remove(name)
                }
            }

            spellsTypesH[name] = i
            spellsTypesMb.items.add(cm)
        }

        for(record in School().all()) {
                val school: School = record as School
            val cm = CheckMenuItem(school.name)
            cm.isSelected = true
            cm.selectedProperty().addListener { observableValue, _, _ ->
                if (observableValue.value){
                    spellsSchoolsH[school.name] = school.id
                } else {
                    spellsSchoolsH.remove(school.name)
                }
            }

            spellsSchoolsH[school.name] = school.id
            spellsSchoolsMb.items.add(cm)
        }
    }

    private fun featureList(isAdvantage: Boolean): ObservableList<Any> {
        var query = "advantage=$isAdvantage and cost != 0 and featureType like "
        val numbers = if(isAdvantage) advantagesH else disadvantagesH
        for ((_, value) in numbers) {
            query += if (query == "advantage=$isAdvantage and cost != 0 and featureType like ") "'%${value + 1}%'"
            else " or advantage=$isAdvantage and cost != 0 and featureType like '%${value + 1}%'"
        }

        if (query == "advantage=$isAdvantage and featureType like ")
            query = "advantage=$isAdvantage and featureType='6'"

        return Feature().where(query)
    }

    private fun skillsList(): ObservableList<Any>? {
        var characteristicsQ = "skillType like "
        var complexitiesQ = " and complexity like "

        for ((_, value) in skillsCharacteristicsH) {
            characteristicsQ += if (characteristicsQ == "skillType like ") "'%$value%'"
            else " or skillType like '%$value%'"
        }

        for ((_, value) in skillsComplexitiesH) {
            complexitiesQ += if (complexitiesQ == " and complexity like ") "'%$value%'"
            else " or complexity like '%$value%'"
        }

        if (characteristicsQ == "skillType like ") characteristicsQ = "skillType='-1'"
        if (complexitiesQ == " and complexity like ") complexitiesQ = " and complexity='-1'"
        return Skill().where(characteristicsQ + complexitiesQ)
    }

    private fun techniquesList(): ObservableList<Any> {
        var query = "complexity like "

        for ((_, value) in techniquesH) {
            query += if (query == "complexity like ") "'%$value%'"
            else " or complexity like '%$value%'"
        }

        if (query == "complexity like ") query = "complexity='-1'"
        return Technique().where(query)
    }

    private fun spellsList(): ObservableList<Any>? {
        var spellsQ = "spellType like "
        var schoolsQ = " and schoolId = "
        for ((_, value) in spellsTypesH) {
            spellsQ += if (spellsQ == "spellType like ") "'%$value%'"
            else " or spellType like '%$value%'"
        }

        for ((_, value) in spellsSchoolsH) {
            schoolsQ += if (schoolsQ == " and schoolId = ") "$value"
            else " or schoolId = $value"
        }

        if (spellsQ == "spellType like ") spellsQ = "spellType='-1'"
        if (schoolsQ == " and schoolId = ") schoolsQ = " and schoolId = -1"

        return Spell().where(spellsQ + schoolsQ)
    }

    private fun tlSet(points: Int) {
        character.tl = points
    }

    private fun characteristicsGenerate(points: Int) {
        var pointsLeft = points
        loop@ while (pointsLeft > 0) {
            var cost = 0
            when (Random().nextInt(100)) {
                0 -> {
                    character.st += 1
                    cost = 10
                    pointsLeft -= cost

                    if (pointsLeft < 0) {
                        character.st -= 1
                        break@loop
                    }
                    character.hp += 1
                }
                1 -> {
                    character.dx += 1
                    cost = 20
                    pointsLeft -= cost
                    if (pointsLeft < 0) {
                        character.dx -= 1
                        break@loop
                    }
                }
                2 -> {
                    character.iq += 1
                    cost = 20
                    pointsLeft -= cost
                    if (pointsLeft < 0) {
                        character.iq -= 1
                        break@loop
                    }
                    character.will += 1
                    character.per += 1
                }
                3 -> {
                    character.ht += 1
                    cost = 10
                    pointsLeft -= cost
                    if (pointsLeft < 0) {
                        character.ht -= 1
                        break@loop
                    }
                    character.fp += 1
                }
            }

            character.currentPoints = cost + character.currentPoints
        }

        character.bs = CharacterParams.defaultBs()
        character.move = character.bs.toInt()
    }

    private fun advantagesGenerate(points: Int) {
        var pointsLeft = points
        val advantages = featureList(true)
        if (advantages.size <= 1) return

        val list = ArrayList<Int>()
        while (pointsLeft > 0 || advantages.size != list.size) {
            if (advantages.size - list.size == 1) break
            val feature = advantages[Random().nextInt(advantages.size - 1)] as Feature
            if (feature.id !in list) {
                list.add(feature.id)
                var level = 1
                if (feature.maxLevel > 1)
                    level = Random().nextInt(feature.maxLevel) + 1
                pointsLeft -= feature.cost * level
                if (pointsLeft < 0) break

                character.currentPoints = feature.cost + character.currentPoints
                CharactersFeature(character.id, feature.id, feature.cost, level, feature.advantage).create()
            }
        }
    }

    private fun disadvantagesGenerate(points: Int) {
        var pointsLeft = points
        val disadvantages = featureList(false)
        if (disadvantages.size <= 1) return
        val list = ArrayList<Int>()
        while (pointsLeft > 0 || disadvantages.size != list.size) {
            if (disadvantages.size - list.size == 1) break
            val feature = disadvantages[Random().nextInt(disadvantages.size - 1)] as Feature
            if (feature.id !in list) {
                list.add(feature.id)
                var level = 1
                if (feature.maxLevel > 1)
                    level = Random().nextInt(feature.maxLevel) + 1
                pointsLeft += feature.cost * level
                if (pointsLeft < 0) break

                character.currentPoints = feature.cost + character.currentPoints
                CharactersFeature(character.id, feature.id, feature.cost, level, feature.advantage).create()
            }
        }
    }

    private fun skillsGenerate(points: Int) {
        var pointsLeft = points
        val skills: ObservableList<Skill> = skillsList() as ObservableList<Skill>
        if (skills.size <= 1) return

        val list = ArrayList<Int>()
        val maxLvl = pointsLeft / 10
        while (pointsLeft > 0 || skills.size != list.size) {
            if (skills.size - list.size == 1) break
            val skill = skills[Random().nextInt(skills.size - 1)]
            if (skill.specializations.any()) {
                if (skill.id !in list) {
                    val specialization = skill.specializations[Random().nextInt(skill.specializations.size - 1)]
                    list.add(skill.id)
                    specialization.level = CharacterParams.skillLevel(specialization)
                    specialization.level += Random().nextInt(maxLvl)
                    specialization.cost = CharacterParams.skillCost(specialization)
                    pointsLeft -= specialization.cost
                    if (pointsLeft < 0) break
                    character.currentPoints = character.currentPoints + specialization.cost
                    CharactersSpecialization(characterId = character.id, itemId = specialization.id, skillId = skill.id,
                            level = specialization.level, cost = specialization.cost).create()
                }
            } else {
                if (skill.id !in list) {
                    list.add(skill.id)
                    skill.level = CharacterParams.skillLevel(skill)
                    skill.level += Random().nextInt(maxLvl)
                    skill.cost = CharacterParams.skillCost(skill)
                    pointsLeft -= skill.cost
                    if (pointsLeft < 0) break
                    character.currentPoints = character.currentPoints + skill.cost
                    CharactersSpecialization(characterId = character.id, skillId = skill.id, itemId = skill.id,
                            level = skill.level, cost =  skill.cost).create()
                }
            }
        }
    }

    private fun techniquesGenerate(points: Int) {
        var pointsLeft = points
        val techniques: ObservableList<Technique> = techniquesList() as ObservableList<Technique>
        if (techniques.size <= 1) return

        val list = ArrayList<Int>()
        val maxLvl = pointsLeft / 10
        while (pointsLeft > 0 || techniques.size != list.size) {
            if (techniques.size - list.size == 1) break
            val technique = techniques[Random().nextInt(techniques.size - 1)]

            if (technique.id !in list) {
                list.add(technique.id)
                technique.level += Random().nextInt(maxLvl)
                technique.cost = CharacterParams.techniqueCost(technique)
                pointsLeft -= technique.cost
                if (pointsLeft < 0) break
                character.currentPoints = character.currentPoints + technique.cost
                CharactersTechnique(character.id, technique.id, technique.level, technique.cost).create()
            }
        }
    }

    private fun spellsGenerate(points: Int) {
        var pointsLeft = points
        val spells: ObservableList<Spell> = spellsList() as ObservableList<Spell>
        if (spells.size <= 1) return
        val list = ArrayList<Int>()
        val maxLvl = pointsLeft / 10
        while (pointsLeft > 0 || spells.size != list.size) {
            if (spells.size - list.size == 1) break
            val spell = spells[Random().nextInt(spells.size - 1)]
            if (spell.id !in list) {
                list.add(spell.id)
                if (spell._complexity == 2) {
                    spell.level = character.iq - 2
                } else {
                    spell.level = character.iq - 3
                }
                spell.level += Random().nextInt(maxLvl)
                spell.finalCost = CharacterParams.spellCost(spell)
                pointsLeft -= spell.finalCost
                if (pointsLeft < 0) break
                character.currentPoints = character.currentPoints + spell.finalCost
                CharactersSpell(character.id, spell.id, spell.level, spell.finalCost).create()
            }
        }
    }

    private fun equipmentsGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<Equipment> = Equipment().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<Equipment>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersEquipment(character.id, item.id, 1).create()
            }
        }
    }

    private fun manArmorGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<ManArmor> = ManArmor().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<ManArmor>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersManArmor(character.id, item.id, 1).create()
            }
        }
    }

    private fun notNamArmorGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<NotManArmor> = NotManArmor().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<NotManArmor>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersNotManArmor(character.id, item.id, 1).create()
            }
        }
    }

    private fun meleeWeaponGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<MeleeWeapon> = MeleeWeapon().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<MeleeWeapon>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersMeleeWeapon(character.id, item.id, 1).create()
            }
        }
    }

    private fun meleeWeaponRangedGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<MeleeWeaponRanged> = MeleeWeaponRanged().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<MeleeWeaponRanged>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersMeleeWeaponRanged(character.id, item.id, 1).create()
            }
        }
    }

    private fun shieldsGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<Shield> = Shield().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<Shield>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersShield(character.id, item.id, 1).create()
            }
        }
    }

    private fun gunsGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<Gun> = Gun().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<Gun>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersGun(character.id, item.id, 1).create()
            }
        }
    }

    private fun grenadesGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<Grenade> = Grenade().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<Grenade>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersGrenade(character.id, item.id, 1).create()
            }
        }
    }

    private fun transportAirGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<TransportsAir> = TransportsAir().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<TransportsAir>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersTransportsAir(character.id, item.id, 1).create()
            }
        }
    }

    private fun transportGroundGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<TransportsGround> = TransportsGround().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<TransportsGround>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersTransportsGround(character.id, item.id, 1).create()
            }
        }
    }

    private fun transportWaterGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<TransportsWater> = TransportsWater().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<TransportsWater>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersTransportsWater(character.id, item.id, 1).create()
            }
        }
    }

    private fun transportSpaceGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<TransportsSpace> = TransportsSpace().where("tl >= 0 and tl <= ${tl.text}") as ObservableList<TransportsSpace>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.cents
                if (centsLeft < 0) break
                character.inventoryCost += item.cents
                CharactersTransportsSpace(character.id, item.id, 1).create()
            }
        }
    }

    private fun alchemiesGenerate(dollars: Int) {
        var centsLeft: Long = (dollars * 100).toLong()
        character.moneyTotal += centsLeft

        val items: ObservableList<Alchemy> = Alchemy().all() as ObservableList<Alchemy>
        val list = ArrayList<Int>()
        while (centsLeft > 0 || items.size != list.size) {
            if (items.size - list.size == 1) break
            val item = items[Random().nextInt(items.size - 1)]
            if (item.id !in list) {
                list.add(item.id)

                centsLeft -= item.recipeCost
                if (centsLeft < 0) break
                character.inventoryCost += item.recipeCost
                CharactersAlchemy(character.id, item.id).create()
            }
        }
    }
}
