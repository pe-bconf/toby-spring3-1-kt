package toby.spring.connect

import toby.spring.config.JDBC_URL
import java.sql.Connection
import java.sql.DriverManager

class DConnectionMaker: ConnectionMaker {
    val dbUser = "user"
    val dbPassword = "pass"

    override fun makeConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")
        return DriverManager.getConnection(JDBC_URL, dbUser, dbPassword)
    }
}