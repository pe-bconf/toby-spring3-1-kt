package toby.spring.user.dao

import toby.spring.user.domain.User

class UserDao {
    private var connectionMaker: ConnectionMaker

    constructor(connectionMaker: ConnectionMaker) {
        this.connectionMaker = connectionMaker
    }

    fun add(user: User) {
        val c = connectionMaker.makeConnection()

        val ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()
        ps.close()
        c.close()
    }

    fun get(id: String): User {
        val c = connectionMaker.makeConnection()

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