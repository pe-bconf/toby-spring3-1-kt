package toby.user.dao

import java.sql.Connection
import java.sql.DriverManager

class SimpleConnectionMaker {
    val dbUser = "user"
    val dbPassword = "pass"

    fun makeNewConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")
        return DriverManager.getConnection(JDBC_URL, dbUser, dbPassword)
    }

}