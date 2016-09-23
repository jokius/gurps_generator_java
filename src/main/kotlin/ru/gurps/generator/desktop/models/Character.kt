package ru.gurps.generator.desktop.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.characters.*
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsAir
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsGround
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsSpace
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsWater
import ru.gurps.generator.desktop.models.rules.*
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.Money
import java.sql.ResultSet
import java.util.*

class Character : Model {
    override var id: Int = -1
    var player: String = ""
    var name: String = ""
    var currentPoints: Int = 0
    var maxPoints: Int = 0
    var st: Int = 10
    var dx: Int = 10
    var iq: Int = 10
    var ht: Int = 10
    var hp: Int = 10
    var will: Int = 10
    var per: Int = 10
    var fp: Int = 10
    var bs: Double = 5.0
    var move: Int = 5
    var sm: Int = 0
    var growth: Int = 0
    var weight: Int = 0
    var age: Int = 0
    var tl: Int = 0
    var tlCost: Int = 0
    var head: Int = 0
    var torse: Int = 0
    var arm: Int = 0
    var leg: Int = 0
    var hand: Int = 0
    var foot: Int = 0
    var noFineManipulators: Boolean = false
    var moneyTotal: Long = 0
    var moneyTotalDollars: Long
        get() = moneyTotal / 100
        set(value) {
            moneyTotal = value * 100
        }

    var inventoryCost: Long = 0

    var moneyLeft: String = ""
        get() = Money.toDollars(moneyTotal - inventoryCost)

    constructor()
    constructor(name: String, maxPoints: Int) {
        this.name = name
        this.maxPoints = maxPoints
    }
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val playerRes = result.getString("player")
        if (playerRes != null) player = playerRes
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        currentPoints = result.getInt("currentPoints")
        maxPoints = result.getInt("maxPoints")
        st = result.getInt("st")
        dx = result.getInt("dx")
        iq = result.getInt("iq")
        ht = result.getInt("ht")
        hp = result.getInt("hp")
        will = result.getInt("will")
        per = result.getInt("per")
        fp = result.getInt("fp")
        bs = result.getDouble("bs")
        move = result.getInt("move")
        sm = result.getInt("sm")
        growth = result.getInt("growth")
        weight = result.getInt("weight")
        age = result.getInt("age")
        tl = result.getInt("tl")
        tlCost = result.getInt("tlCost")
        head = result.getInt("head")
        torse = result.getInt("torse")
        arm = result.getInt("arm")
        leg = result.getInt("leg")
        hand = result.getInt("hand")
        foot = result.getInt("foot")
        noFineManipulators = result.getBoolean("noFineManipulators")
        moneyTotal = result.getLong("moneyTotal")
        inventoryCost = result.getLong("inventoryCost")
    }

    fun alchemies(): ObservableList<Alchemy> {
        val charactersAlchemy = hasMany(CharactersAlchemy()) as ObservableList<CharactersAlchemy>
        val alchemies = FXCollections.observableArrayList<Alchemy>()
        charactersAlchemy.forEach {
            val alchemy = CharactersAlchemy().find(it.itemId) as Alchemy
            alchemy.add = true
            alchemies.add(alchemy)
        }
        return alchemies
    }

    fun features(params: HashMap<String, Any> = hashMapOf()): ObservableList<Feature> {
        val charactersFeatures = hasMany(CharactersFeature(), params) as ObservableList<CharactersFeature>

        val features = FXCollections.observableArrayList<Feature>()
        charactersFeatures.forEach {
            val feature = Feature().find(it.itemId) as Feature
            feature.add = true
            feature.level = it.level
            feature.cost = it.cost
            features.add(feature)
        }
        return features
    }

    fun skills(): ObservableList<Skill> {
        val charactersSkills = hasMany(CharactersSkill()) as ObservableList<CharactersSkill>
        val skills = FXCollections.observableArrayList<Skill>()
        charactersSkills.forEach {
            val skill = Skill().find(it.itemId) as Skill
            skill.add = true
            skill.level = it.level
            skill.cost = it.cost
            skills.add(skill)
        }

        specializations().forEach {
            val skill = Skill().find(it.skillId) as Skill
            val rm = skills.find { currentSkill -> currentSkill.id == it.skillId }
            skills.remove(rm)

            skill.name = "${skill.name} (${it.name})"
            skill.nameEn = "${skill.nameEn} (${it.nameEn})"
            skill.level = it.level
            skill.cost = it.cost
            skill.add = true
            skills.add(skill)
        }

        return skills
    }

    fun specializations(params: HashMap<String, Any> = hashMapOf()): ObservableList<Specialization> {
        val charactersSpec = hasMany(CharactersSpecialization(), params) as ObservableList<CharactersSpecialization>

        val specializations = FXCollections.observableArrayList<Specialization>()
        charactersSpec.forEach {
            val specialization = Specialization().find(it.itemId) as Specialization
            specialization.add = true
            specialization.level = it.level
            specialization.cost = it.cost
            specializations.add(specialization)
        }

        return specializations
    }

    fun techniques(): ObservableList<Technique> {
        val charactersTechnique = hasMany(CharactersTechnique()) as ObservableList<CharactersTechnique>
        val techniques = FXCollections.observableArrayList<Technique>()
        charactersTechnique.forEach {
            val technique = Technique().find(it.itemId) as Technique
            technique.add = true
            technique.level = it.level
            technique.cost = it.cost
            techniques.add(technique)
        }

        return techniques
    }

    fun spells(): ObservableList<Spell> {
        val charactersSpells = hasMany(CharactersSpell()) as ObservableList<CharactersSpell>
        val spells = FXCollections.observableArrayList<Spell>()
        charactersSpells.forEach {
            val spell = Spell().find(it.itemId) as Spell
            spell.add = true
            spell.level = it.level
            spell.finalCost = it.cost
            spells.add(spell)
        }

        return spells
    }

    fun languages(): ObservableList<Language> {
        val charactersLanguages = hasMany(CharactersLanguage()) as ObservableList<CharactersLanguage>
        val languages = FXCollections.observableArrayList<Language>()
        charactersLanguages.forEach {
            val language = Language().find(it.itemId) as Language
            language.spoken = it.spoken
            language.written = it.written
            language.cost = it.cost
            languages.add(language)
        }

        return languages
    }

    fun cultures(): ObservableList<Cultura> {
        val charactersCulturas = hasMany(CharactersCultura()) as ObservableList<CharactersCultura>
        val culturas = FXCollections.observableArrayList<Cultura>()
        charactersCulturas.forEach {
            val cultura = Cultura().find(it.itemId) as Cultura
            cultura.cost = it.cost
            culturas.add(cultura)
        }

        return culturas
    }

    fun quirks(): ObservableList<Quirk> {
        val charactersQuirks = hasMany(CharactersQuirk()) as ObservableList<CharactersQuirk>
        val quirks = FXCollections.observableArrayList<Quirk>()
        charactersQuirks.forEach {
            val quirk = Quirk().find(it.itemId) as Quirk
            quirk.cost = it.cost
            quirks.add(quirk)
        }

        return quirks
    }

    fun shields(): ObservableList<Shield> {
        val characterShields = hasMany(CharactersShield()) as ObservableList<CharactersShield>
        val shields = FXCollections.observableArrayList<Shield>()
        characterShields.forEach {
            val shield = Shield().find(it.itemId) as Shield
            shield.count = it.count
            shield.add = true
            shields.add(shield)
        }

        return shields
    }

    override fun destroy() {
        CharactersAlchemy().destroy_all(CharactersAlchemy().where("characterId", id))
        CharactersFeature().destroy_all(CharactersFeature().where("characterId", id))
        CharactersLanguage().destroy_all(CharactersLanguage().where("characterId", id))
        CharactersSkill().destroy_all(CharactersSkill().where("characterId", id))
        CharactersSpecialization().destroy_all(CharactersSpecialization().where("characterId", id))
        CharactersCultura().destroy_all(CharactersCultura().where("characterId", id))
        CharactersQuirk().destroy_all(CharactersQuirk().where("characterId", id))
        CharactersSpell().destroy_all(CharactersSpell().where("characterId", id))
        CharactersTechnique().destroy_all(CharactersTechnique().where("characterId", id))
        CharactersEquipment().destroy_all(CharactersEquipment().where("characterId", id))
        CharactersManArmor().destroy_all(CharactersManArmor().where("characterId", id))
        CharactersNotManArmor().destroy_all(CharactersNotManArmor().where("characterId", id))
        CharactersMeleeWeapon().destroy_all(CharactersMeleeWeapon().where("characterId", id))
        CharactersMeleeWeaponRanged().destroy_all(CharactersMeleeWeaponRanged().where("characterId", id))
        CharactersShield().destroy_all(CharactersShield().where("characterId", id))
        CharactersGun().destroy_all(CharactersGun().where("characterId", id))
        CharactersGrenade().destroy_all(CharactersGrenade().where("characterId", id))
        CharactersTransportsAir().destroy_all(CharactersTransportsAir().where("characterId", id))
        CharactersTransportsGround().destroy_all(CharactersTransportsGround().where("characterId", id))
        CharactersTransportsWater().destroy_all(CharactersTransportsWater().where("characterId", id))
        CharactersTransportsSpace().destroy_all(CharactersTransportsSpace().where("characterId", id))
        CharactersAlchemy().destroy_all(CharactersAlchemy().where("characterId", id))
        super.destroy()
    }
}
