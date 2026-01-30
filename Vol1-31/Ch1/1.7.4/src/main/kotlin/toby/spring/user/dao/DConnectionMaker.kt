package toby.spring.user.dao

import java.sql.Connection
import java.sql.DriverManager

class DConnectionMaker: ConnectionMaker {
    val dbUser = "user"
    val dbPassword = "pass"

    override fun makeConnection(): Connection {
        Class.forName(dbDriverName)
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }
}