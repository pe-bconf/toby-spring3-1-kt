package toby.spring.user.dao

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.engine.discovery.DiscoverySelectors.selectClass
import org.junit.platform.launcher.listeners.SummaryGeneratingListener
import org.junit.runner.JUnitCore

import org.springframework.context.support.GenericXmlApplicationContext
import toby.spring.user.domain.User



val testTarget = "sql/"
val dbUser = "user"
val dbPassword = "pass"
val dbName = "testdb"

var jdbcUrl = "jdbc:mysql://localhost:3306/springbook?characterEncoding=UTF-8"

class UserDaoTest {
    @Test
    fun addAndGet() {
        // val ctx = AnnotationConfigApplicationContext(DaoFactory::class.java)
        val ctx = GenericXmlApplicationContext("config/applicationContext.xml")

        val dao = ctx.getBean("userDao", UserDao::class.java)
        val user1 = User()
        user1.id = "fortest"
        user1.name = "제이유닛오"
        user1.password = "junit5"

        dao.add(user1)

        println("${user1.id} 등록 성공")

        val user2 = dao.get(user1.id)

        assertThat(user2.name, `is`(user1.name))
        assertThat(user2.password, `is`(user1.password))
    }

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            // JUnit 4
            JUnitCore.main("toby.spring.user.dao.UserDaoTest")

            // JUnit 5
            /*val request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(UserDaoTest::class.java))
                .build()

            val launcher = LauncherFactory.create()
            val listener = SummaryGeneratingListener()

            launcher.registerTestExecutionListeners(listener)
            launcher.execute(request)

            val summary = listener.summary
            println("테스트 결과: ${summary.testsSucceededCount} 성공, ${summary.testsFailedCount} 실패")*/
        }
    }
}