package toby.user.dao

import toby.user.domain.User
import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.getConnection

var JDBC_URL: String = ""
    get() = field
    set(jdbcUrl) {
        field = jdbcUrl
    }

class UserDao {
    private var simpleConnectionMaker: SimpleConnectionMaker

    constructor() {
        simpleConnectionMaker = SimpleConnectionMaker()
    }

    fun addUser(user: User) {
        val c = simpleConnectionMaker.makeNewConnection()

        val ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()
        ps.close()
        c.close()
    }

    fun getUser(id: String): User? {
        val c = simpleConnectionMaker.makeNewConnection()

        val ps = c.prepareStatement("select id, name, password from users where id = ?")
        ps.setString(1, id)

        val rs = ps.executeQuery()
        rs.next()
        val user = User()
        user.id = rs.getString("id")
        user.name = rs.getString("name")
        user.password = rs.getString("password")

        return user
    }
}