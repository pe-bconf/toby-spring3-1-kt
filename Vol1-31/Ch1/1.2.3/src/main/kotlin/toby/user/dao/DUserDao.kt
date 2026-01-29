package toby.user.dao

import java.sql.Connection
import java.sql.DriverManager

class DUserDao : UserDao() {
    override fun getConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")
        return DriverManager.getConnection(JDBC_URL, dbUser, dbPassword)
    }
}