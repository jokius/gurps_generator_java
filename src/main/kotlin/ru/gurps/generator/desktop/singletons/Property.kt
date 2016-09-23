package ru.gurps.generator.desktop.singletons

import ru.gurps.generator.desktop.lib.ExportDb
import tornadofx.*

import java.io.*
import java.net.URLDecoder
import java.util.*

object Property {
    val props: Properties = Properties()

    init {
        val file = File(jarFolder + "properties/all.properties")
        if (!file.exists() || file.isDirectory)
            saveDefaultProperties()
        try {
            props.load(FileInputStream(file))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun saveDefaultProperties() {
        val dir = File(jarFolder + "properties")
        if (!dir.exists() || !dir.isDirectory)
            dir.mkdir()

        props.setProperty("locale", "ru_RU")
        props.setProperty("server_address", "http://gurps.helper.ru")
        props.setProperty("git_address", "https://api.github.com/repos/jokius/gurps_generator_java")
        props.setProperty("new_version_url", "https://github.com/jokius/gurps_generator_java/releases")
        props.setProperty("update_start", "735")
        props.setProperty("db_folder", dbParentFolder() + "db/")
        props.setProperty("db_name", "gurps.mv.db")
        props.setProperty("db_simple_name", "gurps")
        props.setProperty("select_view", "0")
        save()
    }

    private val jarFolder: String
        get() {
            try {
                return URLDecoder.decode(ExportDb::class.java.protectionDomain.codeSource.location.path
                        .replace("\\w*.jar".toRegex(), ""), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return ""
            }

        }

    private fun dbParentFolder(): String {
        return jarFolder
    }

    @Throws(FileNotFoundException::class)
    private fun propertyFile(): OutputStream {
        val f = File(jarFolder + "properties/all.properties")
        return FileOutputStream(f)
    }

    private fun save() {
        try {
            props.store(propertyFile(), null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    val selectViewHash by lazy {
        val params: HashMap<Int, String> = HashMap()
        params[0] = FX.messages["tables"]
        params[1] = FX.messages["character_sheet"]
        params
    }

    var isLastVersion: Boolean? = true

    var lastVersionUrl: String? = "${props.getProperty("git_address")}/releases"

    var serverAddress: String
        get() = props.getProperty("server_address")
        set(serverAddress) {
            props.setProperty("server_address", serverAddress)
            save()
        }

    var gitAddress: String
        get() = props.getProperty("git_address")
        set(serverAddress) {
            props.setProperty("git_address", serverAddress)
            save()
        }

    var newVersionUrl: String
        get() = props.getProperty("new_version_url")
        set(serverAddress) {
            props.setProperty("new_version_url", serverAddress)
            save()
        }

    var updateStart: String
        get() = props.getProperty("update_start")
        set(updateStart) {
            props.setProperty("update_start", updateStart)
            save()
        }

    var dbFolder: String
        get() = props.getProperty("db_folder")
        set(dbFolder) {
            props.setProperty("db_folder", dbFolder)
            save()
        }

    var dbName: String
        get() = props.getProperty("db_name")
        set(dbName) {
            props.setProperty("db_name", dbName)
            save()
        }

    var dbSimpleName: String
        get() = props.getProperty("db_simple_name")
        set(dbSimpleName) {
            props.setProperty("db_simple_name", dbSimpleName)
            save()
        }

    val locale: ResourceBundle
        get() = ResourceBundle.getBundle("bundles.generator", Locale(props.getProperty("locale")))

    fun setLocale(locale: String) {
        props.setProperty("locale", locale)
        save()
    }

    var selectView: String
        get() = selectViewHash[props.getProperty("select_view").toInt()]!!
        set(value) {
            props.setProperty("select_view", selectViewHash.filter { it.value == value }.entries.first().key.toString())
            save()
        }
}