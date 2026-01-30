package toby.spring.user.dao

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import toby.spring.user.domain.User

val dbDriverName = "com.mysql.cj.jdbc.Driver"
val testTarget = "sql/"

var jdbcUrl = ""
val dbUser = "user"
val dbPassword = "pass"
val dbName = "testdb"

class UserDaoTest {
    fun main() {
        val ctx = AnnotationConfigApplicationContext(DaoFactory::class.java)
        val dao = ctx.getBean("userDao", UserDao::class.java)
        val user = User()
        user.id = "tester"
        user.name = "테스터"
        user.password = "test"

        dao.addUser(user)

        println("${user.id} 등록 성공")

        val user2 = dao.getUser(user.id)
        println("이름: ${user2?.name}, 비밀번호: ${user2?.password}")
        println("${user2?.id} 조회 성공")

        val ccm = ctx.getBean("connectionMaker", CountingConnectionMaker::class.java)
        println("Connection counter : ${ccm.counter}")
    }
}