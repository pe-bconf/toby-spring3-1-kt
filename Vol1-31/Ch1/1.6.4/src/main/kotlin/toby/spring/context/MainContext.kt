package toby.spring.context

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import toby.spring.config.DaoFactory
import toby.spring.dao.UserDao
import toby.spring.domain.User
import toby.spring.sandbox.Singleton

class MainContext {
    companion object {
        // @JvmStatic
        fun main(args: Array<String>) {
            val context = AnnotationConfigApplicationContext(DaoFactory::class.java)
            val dao = context.getBean("userDao", UserDao::class.java)
        }
    }

    fun testMain() {
        val ctx = AnnotationConfigApplicationContext(DaoFactory::class.java)
        println(ctx.getBean(UserDao::class.java))
        println(ctx.getBean(UserDao::class.java))

    }
}