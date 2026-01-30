package toby.user.dao

import java.sql.Connection
import java.sql.DriverManager

class DConnectionMaker: ConnectionMaker {
    override fun makeConnection(): Connection {
        Class.forName(dbDriverName)
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }
}