package toby.user.dao

import toby.user.domain.User

val dbDriverName = "com.mysql.cj.jdbc.Driver"
val testTarget = "sql/"

var jdbcUrl = ""
val dbUser = "user"
val dbPassword = "pass"
val dbName = "testdb"

open class UserDao {
    private var simpleConnectionMaker: SimpleConnectionMaker

    constructor() {
        simpleConnectionMaker = SimpleConnectionMaker()
    }

    fun main() {
        // 실제 액션 위치
        val dao = NUserDao()

        val user = User()
        user.id = "tester"
        user.name = "테스터"
        user.password = "test"

        dao.add(user)

        println("${user.id} 등록 성공")

        val user2 = dao.get(user.id)
        println("이름: ${user2.name}, 비밀번호: ${user2.password}")
        println("${user2.id} 조회 성공")
    }


    fun add(user: User) {
        val c = simpleConnectionMaker.makeNewConnection()

        val ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        ps.setString(1, user.id)
        ps.setString(2, user.name)
        ps.setString(3, user.password)

        ps.executeUpdate()
        ps.close()
        c.close()
    }

    fun get(id: String): User {
        val c = simpleConnectionMaker.makeNewConnection()

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