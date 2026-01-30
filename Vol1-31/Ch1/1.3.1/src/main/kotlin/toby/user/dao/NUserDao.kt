package toby.user.dao

import java.sql.Connection
import java.sql.DriverManager

class NUserDao : UserDao() {
    protected fun getConnection(): Connection {
        Class.forName(dbDriverName)
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }
}