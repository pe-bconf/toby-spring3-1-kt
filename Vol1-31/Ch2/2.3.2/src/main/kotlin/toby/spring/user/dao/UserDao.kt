package toby.spring.user.dao

import toby.spring.user.domain.User
import javax.sql.DataSource

class UserDao {
    private lateinit var dataSource: DataSource

    constructor() {}

    fun setDataSource(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    fun add(user: User) {
        val c = dataSource.connection

        val ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()
        ps.close()
        c.close()
    }

    fun get(id: String): User {
        val c = dataSource.connection

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

    fun deleteAll() {
        val c = dataSource.connection

        val ps = c.prepareStatement("delete from users")
        ps.executeUpdate()

        ps.close()
        c.close()
    }

    fun getCount(): Int {
        val c = dataSource.connection

        val ps = c.prepareStatement("select count(*) from users")
        val rs = ps.executeQuery()
        rs.next()
        val count = rs.getInt(1)

        rs.close()
        ps.close()
        c.close()

        return count
    }
}