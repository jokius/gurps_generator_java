package ru.gurps.generator.desktop.config


import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.h2.jdbc.JdbcSQLException
import ru.gurps.generator.desktop.singletons.Property
import tornadofx.FX.Companion.messages
import tornadofx.get
import java.sql.Connection
import java.sql.DriverManager

object Db {
    init { println("Db connect") }
    var connect: Connection = createConnection()
    private fun createConnection(): Connection {
        Class.forName("org.h2.Driver").newInstance()
        val db = "jdbc:h2:${Property.dbFolder}${Property.dbSimpleName}"

        try {
            migrations(db)

        } catch (e: JdbcSQLException) {
            if (e.errorCode == 90020) {
                System.err.println(messages["app_is_running"])
                System.exit(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return DriverManager.getConnection(db, "sa", "")
    }

    private fun migrations(db: String) {
        val flyway = Flyway()
        flyway.setDataSource(db, "sa", "")
        try {
            flyway.migrate()
        } catch (e: FlywayException) {
            System.err.println(e)
            System.err.println(messages["app_is_running"])
            System.exit(0)
        }
    }
}