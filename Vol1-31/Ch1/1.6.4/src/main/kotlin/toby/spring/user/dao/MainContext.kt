package toby.spring.user.dao

import org.springframework.context.annotation.AnnotationConfigApplicationContext

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