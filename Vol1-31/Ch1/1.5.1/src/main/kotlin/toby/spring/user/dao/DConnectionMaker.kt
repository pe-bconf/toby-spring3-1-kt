package toby.spring.dao

import toby.user.dao.dbDriverName
import toby.user.dao.dbPassword
import toby.user.dao.dbUser
import toby.user.dao.jdbcUrl
import java.sql.Connection
import java.sql.DriverManager

class DConnectionMaker: ConnectionMaker {
    override fun makeConnection(): Connection {
        Class.forName(toby.user.dao.dbDriverName)
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }
}