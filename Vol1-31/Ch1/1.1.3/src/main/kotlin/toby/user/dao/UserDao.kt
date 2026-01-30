package toby.user.dao

import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import toby.user.domain.User
import java.sql.DriverManager

val dbDriverName = "com.mysql.cj.jdbc.Driver"
val testTarget = "sql/"

var jdbcUrl = ""
val dbUser = "user"
val dbPassword = "pass"
val dbName = "testdb"

class UserDao {
    fun add(user: User) {
        Class.forName(dbDriverName)
        val c = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)

        val ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()
        ps.close()
        c.close()
    }

    fun get(id: String): User {
        Class.forName(dbDriverName)
        val c = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
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