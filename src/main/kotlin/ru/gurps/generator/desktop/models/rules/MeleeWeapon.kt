package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.interfaces.models.MeleeWeaponInterface
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.characters.CharactersMeleeWeapon
import ru.gurps.generator.desktop.models.characters.CharactersMeleeWeaponRanged
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import tornadofx.*
import tornadofx.FX.Companion.messages
import java.sql.ResultSet
import java.util.*

class MeleeWeapon : Model, MeleeWeaponInterface {
    override var id: Int = -1
    override var skills: String = ""
    override var tl: Int = -1
    override var name: String = ""
    override var damage: String = ""
    var reach: String = ""
    var parry: String = ""
    override var cents: Long = 0
    var cost: String = ""
        get() = Money.toDollars(cents)
    override var weight: String = ""
    override var st: Int = 0
    override var twoHands: Boolean = false
    var training: Boolean = false
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

    val meleeWeaponRanged: MeleeWeaponRanged by lazy {
        MeleeWeaponRanged().find_by("name", name) as MeleeWeaponRanged
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
        val reachRes = result.getString("reach")
        if (reachRes != null) reach = reachRes
        val parryRes = result.getString("parry")
        if (parryRes != null) parry = parryRes
        cents = result.getLong("costCent")
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
        st = result.getInt("st")
        twoHands = result.getBoolean("twoHands")
        training = result.getBoolean("training")
    }

    override fun getTl(): String = TechnicalLevels.list[tl]!!

    fun getSt(): String = st.toString()

    override fun getTwoHands(): String = if (twoHands) messages["yes"] else messages["no"]

    fun getTraining(): String = if (training) messages["yes"] else messages["no"]

    override fun getFinalConstCents(): Long = cents * count

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())

    override fun addToCharacter(character: Character) {
        CharactersMeleeWeapon(character.id, id, count).create()
        if (meleeWeaponRanged.id < 1) return
        CharactersMeleeWeaponRanged(character.id, meleeWeaponRanged.id, count).create()
    }

    override fun removeToCharacter(character: Character) {
        val params = HashMap<String, Any>()
        params["characterId"] = character.id
        params["itemId"] = id
        CharactersMeleeWeapon().find_by(params).destroy()
        if (meleeWeaponRanged.id < 1) return
        params.remove("meleeWeaponId")
        params["itemId"] = meleeWeaponRanged.id
        CharactersMeleeWeaponRanged().find_by(params).destroy()
    }
}
