package ru.gurps.generator.desktop.config

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.Base
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import kotlin.reflect.full.memberProperties

open class Model : Base {
    override var id: Int = -1
    val connect: Connection = Db.connect
    val columns by lazy {
        val results = Db.connect.createStatement().executeQuery("show columns from ${tableName()}")
        val map = arrayListOf<String>()
        while (results.next()) {
            val name = results.getString("field")
            if (name != "ID") map.add(name)
        }
        map
    }

    protected fun tableName(): String {
        var tableName: String = try {
            javaClass.getDeclaredField("tableName").get(this) as String
        } catch (e: NoSuchFieldException) {
            ""
        }

        if (!tableName.isNullOrBlank()) return tableName

        val className = this.javaClass.simpleName

        if (className.substring(className.length - 1) == "y")
            tableName = className.substring(0, className.length - 1) + "ies"
        else
            tableName = className + "s"

        return tableName
    }

    fun create(): Model {
        var names = "(id,"
        var values = "VALUES(DEFAULT,"
        this::class.memberProperties
                .filter { columns.indexOf(it.name.toUpperCase()) != -1 }
                .forEach {
                    names += " ${it.name},"
                    val value = it.getter.call(this)
                    if (value is String) values += " '${value.replace("'", "''")}',"
                    else values += " $value,"

                }
        names = names.substring(0, names.length - 1) + ")"
        values = values.substring(0, values.length - 1) + ")"
        connect.createStatement().executeUpdate("INSERT INTO ${tableName()} $names $values")
        var result = connect.createStatement().executeQuery("SELECT id FROM ${tableName()} ORDER BY id ASC")
        result.last()
        result = connect.createStatement().executeQuery("SELECT * FROM " + tableName() + " WHERE id=" +
                result.getInt("id"))
        result.next()
        return setModel(result)
    }

    fun save() {
        if (id == -1) {
            this.id = create().id
            return
        }

        var params = ""
        this::class.memberProperties
                .filter { columns.indexOf(it.name.toUpperCase()) != -1 }.forEach {
            val value = it.getter.call(this)
            if (value is String) params += "${it.name}='${value.replace("'", "''")}',"
            else params += "${it.name}=$value,"
        }

        params = params.substring(0, params.length - 1)
        connect.createStatement().executeUpdate("UPDATE ${tableName()} SET $params WHERE id=$id")
    }

    fun destroy(id: Int) {
        connect.createStatement().executeUpdate("DELETE FROM ${tableName()} WHERE id=$id")
    }

    open fun destroy() {
        if (id > 0) connect.createStatement().executeUpdate("DELETE FROM ${tableName()} WHERE id=$id")
    }

    open fun destroy_all(models: ObservableList<Any>) {
        if (models.isEmpty()) return
        var params = ""
        models.map { it::class.memberProperties.find { it.name == "id" }!!.getter.call(it) }
                .forEach {
                    if (params == "") params += "id='$it'"
                    else params += " or id='$it'"
                }

        connect.createStatement().executeUpdate("DELETE FROM ${tableName()} WHERE $params")
    }

    fun all(): ObservableList<Any> {
        val list = FXCollections.observableArrayList<Any>()
        val results = connect.prepareStatement("SELECT * FROM ${tableName()}").executeQuery()
        try {
            while (results.next()) {
                list.add(setModel(results))
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2000) return list
            e.printStackTrace()
        }

        return list
    }

    fun find(id: Int): Model {
        val result = connect.createStatement().executeQuery("SELECT * FROM ${tableName()} WHERE id=$id")
        if (result.next()) return setModel(result) else return this
    }

    protected fun hasMany(model: Model, paramsHash: HashMap<String, Any> = hashMapOf()): ObservableList<Any> {
        val list = FXCollections.observableArrayList<Any>()
        paramsHash["${javaClass.simpleName}id"] = id
        val params = hashToParams(paramsHash)

        try {
            val results = connect.createStatement().executeQuery("SELECT * FROM ${model.tableName()} WHERE $params")
            while (results.next()) {
                list.add(model.setModel(results))
            }

        } catch (e: SQLException) {
            if (e.errorCode == 2000) return list
            e.printStackTrace()
        }

        return list
    }

    fun find_by(column: String, value: Any): Model {
        val result = connect.createStatement().executeQuery("SELECT * FROM ${tableName()} WHERE $column='$value'")
        if (result.next()) return setModel(result) else return this
    }

    fun find_by(paramsHash: HashMap<String, Any>): Model {
        var params = ""
        val query: String
        if (paramsHash.isEmpty())
            query = "SELECT * FROM ${tableName()}"
        else {
            for ((key, value) in paramsHash)
                params += "$key='$value' and "

            params = params.substring(0, params.length - 5)
            query = "SELECT * FROM ${tableName()} WHERE $params"
        }

        val result = connect.createStatement().executeQuery(query)
        if (result.next()) return setModel(result) else return this
    }

    fun where(paramsHash: HashMap<String, Any>): ObservableList<Any> {
        val params = hashToParams(paramsHash)
        val query: String
        val list = FXCollections.observableArrayList<Any>()
        query = if (params.isNullOrBlank()) "SELECT * FROM ${tableName()}"
        else "SELECT * FROM ${tableName()} WHERE $params"

        try {
            val results = connect.createStatement().executeQuery(query)
            while (results.next()) {
                list.add(setModel(results))
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2000) return list
            e.printStackTrace()
        }

        return list
    }

    fun where(column: String, value: Any): ObservableList<Any> {
        val list = FXCollections.observableArrayList<Any>()
        try {
            val results = connect.createStatement().executeQuery("SELECT * FROM ${tableName()} WHERE $column=$value")
            while (results.next()) {
                list.add(setModel(results))
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2000) return list
            e.printStackTrace()
        }

        return list
    }

    fun where(query: String): ObservableList<Any> {
        val list = FXCollections.observableArrayList<Any>()
        try {
            val results = connect.createStatement().executeQuery("SELECT * FROM ${tableName()} WHERE $query")
            while (results.next()) {
                list.add(setModel(results))
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2000) return list
            e.printStackTrace()
        }

        return list
    }

    fun pluck(column: String): ObservableList<String> {
        val list = FXCollections.observableArrayList<String>()
        try {
            val results = connect.createStatement().executeQuery("SELECT $column FROM ${tableName()}")
            while (results.next()) {
                list.add(results.getString(column))
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2000) return list
            e.printStackTrace()
        }

        return list
    }

    fun pluck(column: String, paramsHash: HashMap<String, Any>): ObservableList<String> {
        val list = FXCollections.observableArrayList<String>()
        val params = hashToParams(paramsHash)
        try {
            val results = connect.createStatement().executeQuery("SELECT $column FROM ${tableName()} WHERE $params")
            while (results.next()) {
                list.add(results.getString(column))
            }
        } catch (e: SQLException) {
            if (e.errorCode == 2000) return list
            e.printStackTrace()
        }

        return list
    }

    private fun hashToParams(paramsHash: HashMap<String, Any>): String {
        if (paramsHash.isEmpty()) return ""
        var params = ""
        paramsHash.forEach { params += "${it.key}='${it.value}' and " }
        return params.substring(0, params.length - 5)
    }

    private fun setModel(result: ResultSet): Model {
        return this::class.java.getConstructor(ResultSet::class.java).newInstance(result)
    }
}
