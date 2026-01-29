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
        val user = User()
        user.id = "tester"
        user.name = "테스터"
        user.password = "test"

        dao.addUser(user)

        println("${user.id} 등록성공")

        val user2 = dao.getUser(user.id)
        println("이름: ${user2?.name}, 비밀번호: ${user2?.password}")
        println("${user2?.id} 조회 성공")
    }
}