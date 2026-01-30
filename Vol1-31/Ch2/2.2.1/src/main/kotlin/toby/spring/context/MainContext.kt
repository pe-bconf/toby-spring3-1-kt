package toby.spring.context

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.GenericXmlApplicationContext
import toby.spring.config.DaoFactory
import toby.spring.dao.UserDao
import toby.spring.domain.User

class MainContext {
    companion object {
        // @JvmStatic
        fun main(args: Array<String>) {
            // val ctx = AnnotationConfigApplicationContext(DaoFactory::class.java)
            val ctx = GenericXmlApplicationContext("config/applicationContext.xml")
            val dao = ctx.getBean("userDao", UserDao::class.java)
        }
    }

    fun testMain() {
        // val ctx = AnnotationConfigApplicationContext(DaoFactory::class.java)
        val ctx = GenericXmlApplicationContext("config/applicationContext.xml")

        val dao = ctx.getBean("userDao", UserDao::class.java)
        val user1 = User()
        user1.id = "tester"
        user1.name = "테스터"
        user1.password = "test"

        dao.addUser(user1)

        println("${user1.id} 등록 성공")

        val user2 = dao.getUser(user1.id)

        if (!user1.name.equals(user2.name)) {
            println("테스트 실패 (name)")
        } else if (!user1.password.equals(user2.password)) {
            println("테스트 실패 (password)")
        } else {
            println("${user2.name} 조회 성공")
        }
    }
}