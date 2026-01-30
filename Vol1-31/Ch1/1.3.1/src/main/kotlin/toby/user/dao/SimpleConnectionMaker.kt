package toby.user.dao

import java.sql.Connection
import java.sql.DriverManager

class SimpleConnectionMaker {
    fun makeNewConnection(): Connection {
        Class.forName(dbDriverName)
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }
}