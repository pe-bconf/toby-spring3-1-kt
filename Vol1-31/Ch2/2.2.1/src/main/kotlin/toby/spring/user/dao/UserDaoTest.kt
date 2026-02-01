package toby.spring.user.dao

import org.springframework.context.support.GenericXmlApplicationContext
import toby.spring.user.domain.User

val testTarget = "sql/"

var jdbcUrl = ""
val dbUser = "user"
val dbPassword = "pass"
val dbName = "testdb"

class UserDaoTest {
    fun main() {
        // val ctx = AnnotationConfigApplicationContext(DaoFactory::class.java)
        val ctx = GenericXmlApplicationContext("config/applicationContext.xml")

        val dao = ctx.getBean("userDao", UserDao::class.java)
        val user1 = User()
        user1.id = "tester"
        user1.name = "테스터"
        user1.password = "test"

        dao.add(user1)

        println("${user1.id} 등록 성공")

        val user2 = dao.get(user1.id)

        if (!user1.name.equals(user2.name)) {
            println("테스트 실패 (name)")
        } else if (!user1.password.equals(user2.password)) {
            println("테스트 실패 (password)")
        } else {
            println("${user2.name} 조회 성공")
        }
    }
}