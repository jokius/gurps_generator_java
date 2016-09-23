package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.MeleeWeaponInterface
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.models.characters.CharactersMeleeWeapon
import ru.gurps.generator.desktop.models.characters.CharactersMeleeWeaponRanged
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import tornadofx.*
import java.sql.ResultSet
import java.util.*

class MeleeWeaponRanged : Model, MeleeWeaponInterface {
    override var id: Int = -1
    override var skills: String = ""
    override var tl: Int = -1
    override var name: String = ""
    override var damage: String = ""
    var accuracy: String = ""
    var range: String = ""
    override var weight: String = ""
    var rateOfFire: Int = 0
    var shots: String = ""
    override var st: Int = 0
    override var cents: Long = 0
    var cost: String = ""
        get() = Money.toDollars(cents)
    var bulk: Int = 0
    override var twoHands: Boolean = false
    override var count: Int = 1
    override var add: Boolean = false
    override var legalityClass: Int = 0
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::MeleeWeapon"
        params["itemId"] = id
        val notesIds = "id in (${NoteItem().pluck("noteId", params).joinToString { it }})"
        Note().where(notesIds) as ObservableList<Note>
    }

    val meleeWeapon: MeleeWeapon by lazy {
        MeleeWeapon().find_by("name", name) as MeleeWeapon
    }

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val skillsRes = result.getString("skills")
        if (skillsRes != null) skills = skillsRes
        tl = result.getInt("tl")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val damageRes = result.getString("damage")
        if (damageRes != null) damage = damageRes
        val accuracyRes = result.getString("accuracy")
        if (accuracyRes != null) accuracy = accuracyRes
        val rangeRes = result.getString("range")
        if (rangeRes != null) range = rangeRes
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
        rateOfFire = result.getInt("rateOfFire")
        val shotsRes = result.getString("shots")
        if (shotsRes != null) shots = shotsRes
        st = result.getInt("st")
        cents = result.getLong("costCent")
        bulk = result.getInt("bulk")
        twoHands = result.getBoolean("twoHands")
    }

    override fun getTl(): String = TechnicalLevels.list[tl]!!

    fun getSt(): String = st.toString()

    override fun getTwoHands(): String = if (twoHands) FX.messages["yes"] else FX.messages["no"]

    override fun getFinalConstCents(): Long = cents * count

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())

    override fun addToCharacter(character: Character) {
        CharactersMeleeWeaponRanged(character.id, id, count).create()
        if (meleeWeapon.id < 1) return
        CharactersMeleeWeapon(character.id, meleeWeapon.id, count).create()
    }

    override fun removeToCharacter(character: Character) {
        val params = HashMap<String, Any>()
        params["characterId"] = character.id
        params["itemId"] = id
        CharactersMeleeWeaponRanged().find_by(params).destroy()
        if (meleeWeapon.id < 1) return
        params["itemId"] = meleeWeapon.id
        CharactersMeleeWeapon().find_by(params).destroy()
    }
}
