package ru.gurps.generator.desktop.fragments

import javafx.scene.layout.AnchorPane
import javafx.scene.web.WebView
import ru.gurps.generator.desktop.controllers.NewItemsController
import ru.gurps.generator.desktop.models.rules.*
import ru.gurps.generator.desktop.models.rules.transports.*
import ru.gurps.generator.desktop.singletons.Property
import tornadofx.Fragment
import javax.json.JsonArray
import javax.json.JsonObject

class HaveUpdatesFragment : Fragment("") {
    override val root: AnchorPane by fxml("/templates/HaveUpdatesFragment.fxml")
    private val newItemsController: NewItemsController by inject()

    private val webView: WebView by fxid()
    private var changeLog: String = "<html>"
    private val jsonRoot: JsonObject = newItemsController.updateFromServer()

    init {
        buildChangeLog()
        webView.engine.loadContent(changeLog)
    }

    private fun accept() {
        jsonRoot.array("models").forEach { modelUpdates(it as JsonObject) }

        Property.updateStart = jsonRoot.int("version").toString()
        close()
    }

    private fun cancel() {
        close()
    }

    private fun buildChangeLog() {
        changeLog += "<head><link rel='stylesheet' href='${resources["/css/main.css"]}'> </head><body>"
        jsonRoot.array("models").forEach { objectSet(it as JsonObject) }
        changeLog += "</html></body>"
    }

    private fun objectSet(entry: JsonObject) {
        val arr = entry.array("changeLog")
        if (arr.size == 0) return
        changeLog += "<h3>${entry.string("human")}</h3>"

        arr.forEach {
            val element: JsonObject = it as JsonObject
            changeLog += "<p>${element.string("name")}"
            changeLog += if(element.isNull("name_en")) "</p>"
            else "(${element.string("name_en")})</p>"
            changeLog += element.string("change_info")
        }
    }

    private fun modelUpdates(element: JsonObject) {
        val name = element.string("name")
        element.array("changeLog").forEach { updateRecord(name, it as JsonObject) }
    }

    private fun updateRecord(name: String, element: JsonObject) {
        val rawInfo = element.obj("raw_info")
        when(name){
            "Rules::Addon" -> {
                val record = Addon().find(rawInfo.getInt("id")) as Addon
                if (rawInfo.getJsonNumber("feature_id") != null) record.featureId = rawInfo.int("feature_id")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (rawInfo.getJsonNumber("cost") != null) record.cost = rawInfo.int("cost")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (rawInfo.getJsonNumber("max_level") != null) record.maxLevel = rawInfo.int("max_level")
                record.save()
            }

            "Rules::Alchemy" -> {
                val record = Alchemy().find(rawInfo.getInt("id")) as Alchemy
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (!rawInfo.string("alternative_names").isBlank())
                    record.alternativeNames = rawInfo.string("alternative_names")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (!rawInfo.string("duration").isBlank()) record.duration = rawInfo.string("duration")
                if (!rawInfo.string("form").isBlank()) record.form = rawInfo.string("form")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("recipe").isBlank()) record.recipe = rawInfo.string("recipe")
                record.save()
            }

            "Rules::Armors::Addon" -> {
                val record = ArmorsAddon().find(rawInfo.getInt("id")) as ArmorsAddon
                if (!rawInfo.string("armor_type").isBlank()) record.armorType = rawInfo.string("armor_type")
                if (rawInfo.getJsonNumber("armor_id") != null) record.armorId = rawInfo.int("armor_id")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("protection").isBlank()) record.protection = rawInfo.string("protection")
                if (!rawInfo.string("damage_resist").isBlank())
                    record.damageResist = rawInfo.string("damage_resist")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("weight").isBlank()) record.weight = rawInfo.string("weight")
                record.save()
            }

            "Rules::Cultura" -> {
                val record = Cultura().find(rawInfo.getInt("id")) as Cultura
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                record.save()
            }

            "Rules::Equipment" -> {
                val record = Equipment().find(rawInfo.getInt("id")) as Equipment
                if (rawInfo.getJsonNumber("application_area_id") != null)
                    record.applicationAreaId = rawInfo.int("application_area_id")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("weight").isBlank()) record.weight = rawInfo.string("weight")
                if (!rawInfo.string("time").isBlank()) record.time = rawInfo.string("time")
                if (!rawInfo.string("legality_class").isBlank())
                    record.legalityClass = rawInfo.string("legality_class").toInt()
                record.save()
            }

            "Rules::Equipments::ApplicationArea" -> {
                val record = EquipmentsApplicationArea().find(rawInfo.getInt("id")) as EquipmentsApplicationArea
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                record.save()
            }

            "Rules::Feature" -> {
                val record = Feature().find(rawInfo.getInt("id")) as Feature
                record.advantage = rawInfo.getBoolean("advantage", record.advantage)
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (!rawInfo.string("feature_type").isBlank()) record.featureType = rawInfo.string("feature_type")
                if (rawInfo.getJsonNumber("cost") != null) record.cost = rawInfo.int("cost")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (rawInfo.getJsonNumber("max_level") != null) record.maxLevel = rawInfo.int("max_level")
                record.psi = rawInfo.getBoolean("psi", record.psi)
                record.cybernetic = rawInfo.getBoolean("cybernetic", record.cybernetic)
                record.save()
            }

            "Rules::Grenade" -> {
                val record = Grenade().find(rawInfo.getInt("id")) as Grenade
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("damage").isBlank()) record.damage = rawInfo.string("damage")
                if (rawInfo.getJsonNumber("weight") != null) record.weight = rawInfo.int("weight").toString()
                if (!rawInfo.string("fuse").isBlank()) record.fuse = rawInfo.string("fuse")
                if (rawInfo.getJsonNumber("cost") != null) record.cost = rawInfo.int("cost").toString()
                if (rawInfo.getJsonNumber("legality_class") != null)
                    record.legalityClass = rawInfo.int("legality_class")
                record.save()
            }

            "Rules::Gun" -> {
                val record = Gun().find(rawInfo.getInt("id")) as Gun
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("damage").isBlank()) record.damage = rawInfo.string("damage")
                if (!rawInfo.string("accuracy").isBlank()) record.accuracy = rawInfo.string("accuracy")
                if (!rawInfo.string("range").isBlank()) record.range = rawInfo.string("range")
                if (!rawInfo.string("weight").isBlank()) record.weight = rawInfo.string("weight")
                if (rawInfo.getJsonNumber("rate_of_fire") != null) record.rateOfFire = rawInfo.int("rate_of_fire")
                if (!rawInfo.string("shots").isBlank()) record.shots = rawInfo.string("shots")
                if (rawInfo.getJsonNumber("st") != null) record.st = rawInfo.int("st")
                if (rawInfo.getJsonNumber("bulk") != null) record.bulk = rawInfo.int("bulk")
                if (rawInfo.getJsonNumber("recoil") != null) record.recoil = rawInfo.int("recoil")
                if (rawInfo.getJsonNumber("cost") != null) record.cost = rawInfo.int("cost").toString()
                if (rawInfo.getJsonNumber("legality_class") != null)
                    record.legalityClass = rawInfo.int("legality_class")
                record.save()
            }

            "Rules::Language" -> {
                val record = Language().find(rawInfo.getInt("id")) as Language
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                record.save()
            }

            "Rules::ManArmor" -> {
                val record = ManArmor().find(rawInfo.getInt("id")) as ManArmor
                if (!rawInfo.string("slot").isBlank()) record.slot = rawInfo.string("slot")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("protection").isBlank()) record.protection = rawInfo.string("protection")
                if (!rawInfo.string("damage_resist").isBlank())
                    record.damageResist = rawInfo.string("damage_resist")
                if (!rawInfo.string("weight").isBlank()) record.weight = rawInfo.string("weight")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("legality_class").isBlank())
                    record.legalityClass = rawInfo.string("legality_class").toInt()
                record.save()
            }

            "Rules::MeleeWeapon" -> {
                val record = MeleeWeapon().find(rawInfo.getInt("id")) as MeleeWeapon
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("damage").isBlank()) record.damage = rawInfo.string("damage")
                if (!rawInfo.string("reach").isBlank()) record.reach = rawInfo.string("reach")
                if (!rawInfo.string("parry").isBlank()) record.parry = rawInfo.string("parry")
                if (rawInfo.getJsonNumber("cost") != null) record.cost = rawInfo.int("cost").toString()
                if (rawInfo.getJsonNumber("weight") != null) record.weight = rawInfo.int("weight").toString()
                if (rawInfo.getJsonNumber("st") != null) record.st = rawInfo.int("st")
                record.twoHands = rawInfo.getBoolean("two_hands", record.twoHands)
                record.training = rawInfo.getBoolean("training", record.training)
                record.save()
            }

            "Rules::MeleeWeaponRanged" -> {
                val record = MeleeWeaponRanged().find(rawInfo.getInt("id")) as MeleeWeaponRanged
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("damage").isBlank()) record.damage = rawInfo.string("damage")
                if (!rawInfo.string("accuracy").isBlank()) record.accuracy = rawInfo.string("accuracy")
                if (!rawInfo.string("range").isBlank()) record.range = rawInfo.string("range")
                if (!rawInfo.string("weight").isBlank()) record.weight = rawInfo.string("weight")
                if (rawInfo.getJsonNumber("rate_of_fire") != null) record.rateOfFire = rawInfo.int("rate_of_fire")
                if (!rawInfo.string("shots").isBlank()) record.shots = rawInfo.string("shots")
                if (rawInfo.getJsonNumber("st") != null) record.st = rawInfo.int("st")
                if (rawInfo.getJsonNumber("cost") != null) record.cost = rawInfo.int("cost").toString()
                if (rawInfo.getJsonNumber("bulk") != null) record.bulk = rawInfo.int("bulk")
                record.twoHands = rawInfo.getBoolean("two_hands", record.twoHands)
                record.save()
            }

            "Rules::Modifier" -> {
                val record = Modifier().find(rawInfo.getInt("id")) as Modifier
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (rawInfo.getJsonNumber("cost") != null) record.cost = rawInfo.int("cost")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (rawInfo.getJsonNumber("max_level") != null) record.maxLevel = rawInfo.int("max_level")
                record.combat = rawInfo.getBoolean("combat", record.combat)
                record.improving = rawInfo.getBoolean("improving", record.improving)
                record.save()
            }

            "Rules::Note" -> {
                val record = Note().find(rawInfo.getInt("id")) as Note
                if (!rawInfo.string("item_type").isBlank()) record.itemType = rawInfo.string("item_type")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                record.save()
            }

            "Rules::NoteItem" -> {
                val record = NoteItem().find(rawInfo.getInt("id")) as NoteItem
                if (!rawInfo.string("item_type").isBlank()) record.itemType = rawInfo.string("item_type")
                if (rawInfo.getJsonNumber("item_id") != null) record.itemId = rawInfo.int("item_id")
                if (rawInfo.getJsonNumber("note_id") != null) record.noteId = rawInfo.int("note_id")
                record.save()
            }

            "Rules::NotManArmor" -> {
                val record = NotManArmor().find(rawInfo.getInt("id")) as NotManArmor
                if (!rawInfo.string("race").isBlank()) record.race = rawInfo.string("race")
                if (!rawInfo.string("slot").isBlank()) record.slot = rawInfo.string("slot")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("protection").isBlank()) record.protection = rawInfo.string("protection")
                if (!rawInfo.string("damage_resist").isBlank())
                    record.damageResist = rawInfo.string("damage_resist")
                if (!rawInfo.string("weight").isBlank()) record.weight = rawInfo.string("weight")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("legality_class").isBlank())
                    record.legalityClass = rawInfo.string("legality_class").toInt()
                record.save()
            }

            "Rules::Shield" -> {
                val record = Shield().find(rawInfo.getInt("id")) as Shield
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (rawInfo.getJsonNumber("defense_bonus") != null) record.defenseBonus = rawInfo.int("defense_bonus")
                if (!rawInfo.string("weight").isBlank()) record.weight = rawInfo.string("weight")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("dr_hp").isBlank()) record.drHp = rawInfo.string("dr_hp")
                if (!rawInfo.string("legality_class").isBlank())
                    record.legalityClass = rawInfo.string("legality_class").toInt()
                record.save()
            }

            "Rules::Quirk" -> {
                val record = Quirk().find(rawInfo.getInt("id")) as Quirk
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                record.save()
            }

            "Rules::School" -> {
                val record = School().find(rawInfo.getInt("id")) as School
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                record.save()
            }

            "Rules::Skill" -> {
                val record = Skill().find(rawInfo.getInt("id")) as Skill
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (rawInfo.getJsonNumber("skill_type") != null) record._skillType = rawInfo.int("skill_type")
                if (!rawInfo.string("default_use").isBlank()) record.defaultUse = rawInfo.string("default_use")
                if (!rawInfo.string("demands").isBlank()) record._demands = rawInfo.string("demands")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (!rawInfo.string("modifiers").isBlank()) record._modifiers = rawInfo.string("modifiers")
                record.twoHands = rawInfo.getBoolean("two_hands", record.twoHands)
                record.parry = rawInfo.getBoolean("parry", record.parry)
                if (rawInfo.getJsonNumber("parry_bonus") != null) record.parryBonus = rawInfo.int("parry_bonus")
                record.save()
            }

            "Rules::Specialization" -> {
                val record = Specialization().find(rawInfo.getInt("id")) as Specialization
                if (rawInfo.getJsonNumber("skill_id") != null) record.skillId = rawInfo.int("skill_id")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (rawInfo.getJsonNumber("skill_type") != null) record._skillType = rawInfo.int("skill_type")
                if (!rawInfo.string("default_use").isBlank()) record.defaultUse = rawInfo.string("default_use")
                if (!rawInfo.string("demands").isBlank()) record._demands = rawInfo.string("demands")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (!rawInfo.string("modifiers").isBlank()) record.modifiers = rawInfo.string("modifiers")
                record.parry = rawInfo.getBoolean("parry", record.parry)
                if (rawInfo.getJsonNumber("parry_bonus") != null) record.parryBonus = rawInfo.int("parry_bonus")
                record.save()
            }

            "Rules::Spell" -> {
                val record = Spell().find(rawInfo.getInt("id")) as Spell
                if (rawInfo.getJsonNumber("school_id") != null) record.schoolId = rawInfo.int("school_id")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (!rawInfo.string("spell_type").isBlank()) record.spellType = rawInfo.string("spell_type")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (rawInfo.getJsonNumber("complexity") != null) record._complexity = rawInfo.int("complexity")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("need_time").isBlank()) record.needTime = rawInfo.string("need_time")
                if (!rawInfo.string("duration").isBlank()) record.duration = rawInfo.string("duration")
                if (!rawInfo.string("maintaining_cost").isBlank())
                    record.maintainingCost = rawInfo.string("maintaining_cost")
                if (!rawInfo.string("thing").isBlank()) record.thing = rawInfo.string("thing")
                if (!rawInfo.string("create_cost").isBlank()) record.createCost = rawInfo.string("create_cost")
                if (!rawInfo.string("demands").isBlank()) record.demands = rawInfo.string("demands")
                if (!rawInfo.string("resistance").isBlank()) record.resistance = rawInfo.string("resistance")
                if (!rawInfo.string("modifiers").isBlank()) record.modifiers = rawInfo.string("modifiers")
                record.save()
            }

            "Rules::Technique" -> {
                val record = Technique().find(rawInfo.getInt("id")) as Technique
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (!rawInfo.string("name_en").isBlank()) record.nameEn = rawInfo.string("name_en")
                if (rawInfo.getJsonNumber("complexity") != null) record._complexity = rawInfo.int("complexity")
                if (!rawInfo.string("default_use").isBlank()) record._defaultUse = rawInfo.string("default_use")
                if (!rawInfo.string("demands").isBlank()) record._demands = rawInfo.string("demands")
                if (!rawInfo.string("description").isBlank()) record.description = rawInfo.string("description")
                record.save()
            }

            "Rules::Transports::Air" -> {
                val record = TransportsAir().find(rawInfo.getInt("id")) as TransportsAir
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (rawInfo.getJsonNumber("st_hp") != null) record.stHp = rawInfo.int("st_hp")
                if (rawInfo.getJsonNumber("handling") != null) record.handling = rawInfo.int("handling")
                if (!rawInfo.string("stability_rating").isBlank())
                    record.stabilityRating = rawInfo.string("stability_rating")

                if (!rawInfo.string("ht").isBlank()) record.ht = rawInfo.string("ht")
                if (!rawInfo.string("move").isBlank()) record.move = rawInfo.string("move")
                if (rawInfo.getJsonNumber("loaded_weight") != null)
                    record.loadedWeight = rawInfo.double("loaded_weight")
                if (rawInfo.getJsonNumber("size_modifier") != null)
                    record.sizeModifier = rawInfo.int("size_modifier").toString()
                if (!rawInfo.string("occupant").isBlank()) record.occupant = rawInfo.string("occupant")
                if (!rawInfo.string("damage_resist").isBlank())
                    record.damageResist = rawInfo.string("damage_resist")
                if (!rawInfo.string("range").isBlank()) record.range = rawInfo.string("range")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (rawInfo.getJsonNumber("stall") != null) record.stall = rawInfo.int("stall")
                record.save()
            }

            "Rules::Transports::Ground" -> {
                val record = TransportsGround().find(rawInfo.getInt("id")) as TransportsGround
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (rawInfo.getJsonNumber("st_hp") != null) record.stHp = rawInfo.int("st_hp")
                if (rawInfo.getJsonNumber("handling") != null) record.handling = rawInfo.int("handling")
                if (!rawInfo.string("stability_rating").isBlank())
                    record.stabilityRating = rawInfo.string("stability_rating")

                if (!rawInfo.string("ht").isBlank()) record.ht = rawInfo.string("ht")
                if (!rawInfo.string("move").isBlank()) record.move = rawInfo.string("move")
                if (rawInfo.getJsonNumber("loaded_weight") != null)
                    record.loadedWeight = rawInfo.double("loaded_weight")
                if (rawInfo.getJsonNumber("size_modifier") != null)
                    record.sizeModifier = rawInfo.int("size_modifier").toString()
                if (!rawInfo.string("occupant").isBlank()) record.occupant = rawInfo.string("occupant")
                if (!rawInfo.string("damage_resist").isBlank())
                    record.damageResist = rawInfo.string("damage_resist")
                if (!rawInfo.string("range").isBlank()) record.range = rawInfo.string("range")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("locations").isBlank()) record.locations = rawInfo.string("locations")
                record.unpowered = rawInfo.getBoolean("unpowered", record.unpowered)
                record.road = rawInfo.getBoolean("road", record.road)
                record.rails = rawInfo.getBoolean("rails", record.rails)
                record.save()
            }

            "Rules::Transports::Space" -> {
                val record = TransportsSpace().find(rawInfo.getInt("id")) as TransportsSpace
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (rawInfo.getJsonNumber("st_hp") != null) record.stHp = rawInfo.int("st_hp")
                if (rawInfo.getJsonNumber("handling") != null) record.handling = rawInfo.int("handling")
                if (!rawInfo.string("stability_rating").isBlank())
                    record.stabilityRating = rawInfo.string("stability_rating")

                if (!rawInfo.string("ht").isBlank()) record.ht = rawInfo.string("ht")
                if (!rawInfo.string("move").isBlank()) record.move = rawInfo.string("move")
                if (rawInfo.getJsonNumber("loaded_weight") != null)
                    record.loadedWeight = rawInfo.double("loaded_weight")
                if (rawInfo.getJsonNumber("size_modifier") != null)
                    record.sizeModifier = rawInfo.int("size_modifier").toString()
                if (!rawInfo.string("occupant").isBlank()) record.occupant = rawInfo.string("occupant")
                if (!rawInfo.string("damage_resist").isBlank())
                    record.damageResist = rawInfo.string("damage_resist")
                if (!rawInfo.string("range").isBlank()) record.range = rawInfo.string("range")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("locations").isBlank()) record.locations = rawInfo.string("locations")
                record.save()
            }

            "Rules::Transports::Water" -> {
                val record = TransportsWater().find(rawInfo.getInt("id")) as TransportsWater
                if (!rawInfo.string("skills").isBlank()) record.skills = rawInfo.string("skills")
                if (rawInfo.getJsonNumber("tl") != null) record.tl = rawInfo.int("tl")
                if (!rawInfo.string("name").isBlank()) record.name = rawInfo.string("name")
                if (rawInfo.getJsonNumber("st_hp") != null) record.stHp = rawInfo.int("st_hp")
                if (rawInfo.getJsonNumber("handling") != null) record.handling = rawInfo.int("handling")
                if (!rawInfo.string("stability_rating").isBlank())
                    record.stabilityRating = rawInfo.string("stability_rating")

                if (!rawInfo.string("ht").isBlank()) record.ht = rawInfo.string("ht")
                if (!rawInfo.string("move").isBlank()) record.move = rawInfo.string("move")
                if (rawInfo.getJsonNumber("loaded_weight") != null)
                    record.loadedWeight = rawInfo.double("loaded_weight")
                if (rawInfo.getJsonNumber("size_modifier") != null)
                    record.sizeModifier = rawInfo.int("size_modifier").toString()
                if (!rawInfo.string("occupant").isBlank()) record.occupant = rawInfo.string("occupant")
                if (!rawInfo.string("damage_resist").isBlank())
                    record.damageResist = rawInfo.string("damage_resist")
                if (!rawInfo.string("range").isBlank()) record.range = rawInfo.string("range")
                if (!rawInfo.string("cost").isBlank()) record.cost = rawInfo.string("cost")
                if (!rawInfo.string("locations").isBlank()) record.locations = rawInfo.string("locations")
                if (rawInfo.getJsonNumber("draft") != null) record.draft = rawInfo.int("draft")
                record.unpowered = rawInfo.getBoolean("unpowered", record.unpowered)
                record.save()
            }
        }
    }

    private fun JsonObject.string(name: String): String {
        val st = getJsonString(name)
        return if (st != null) st.string
        else ""
    }

    private fun JsonObject.int(name: String): Int {
        return getJsonNumber(name).intValue()
    }

    private fun JsonObject.double(name: String): Double {
        return getJsonNumber(name).doubleValue()
    }

    private fun JsonObject.array(name: String): JsonArray {
        return getJsonArray(name)
    }

    private fun JsonObject.obj(name: String): JsonObject {
        return getJsonObject(name)
    }
}
