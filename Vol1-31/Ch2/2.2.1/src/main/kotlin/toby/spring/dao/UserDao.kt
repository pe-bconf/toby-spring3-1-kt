package toby.spring.dao

import toby.spring.domain.User
import javax.sql.DataSource

class UserDao {
    private lateinit var dataSource: DataSource

    constructor() {}

    fun setDataSource(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    fun addUser(user: User) {
        val c = dataSource.connection

        val ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()
        ps.close()
        c.close()
    }

    fun getUser(id: String): User {
        val c = dataSource.connection

        val ps = c.prepareStatement("select id, name, password from users where id = ?")
        ps.setString(1, id)

        val rs = ps.executeQuery()
        rs.next()
        val user = User()
        user.id = rs.getString("id")
        // 실패 테스트 시 주석처리 지점
        user.name = rs.getString("name")
        user.password = rs.getString("password")

        return user
    }
}