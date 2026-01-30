package toby.user.dao

import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import toby.user.domain.User
import java.sql.Connection
import java.sql.DriverManager

var JDBC_URL: String = ""
    get() = field
    set(jdbcUrl) {
        field = jdbcUrl
    }

class UserDao {
    val dbDriverName = "com.mysql.cj.jdbc.Driver"
    val dbName = "testdb"
    val dbUser = "user"
    val dbPassword = "pass"

    constructor() {}

    fun getConnection(): Connection {
        Class.forName(dbDriverName)
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }

    fun add(user: User) {
        val c = getConnection()

        val ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()
        ps.close()
        c.close()
    }

    fun get(id: String): User {
        val c = getConnection()

        val ps = c.prepareStatement("select id, name, password from users where id = ?")
        ps.setString(1, id)

        val rs = ps.executeQuery()
        rs.next()
        val user = User()
        user.id = rs.getString("id")
        user.name = rs.getString("name")
        user.password = rs.getString("password")

        rs.close()
        ps.close()
        c.close()

        return user
    }
}