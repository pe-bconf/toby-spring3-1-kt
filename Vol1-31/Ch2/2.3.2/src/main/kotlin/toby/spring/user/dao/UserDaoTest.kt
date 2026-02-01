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

        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        val user1 = User(id="fortest", name="제이유닛오", password="junit5")
        dao.add(user1)
        assertThat(dao.getCount(), `is`(1))

        println("${user1.id} 등록 성공")

        val user2 = dao.get(user1.id)

        assertThat(user2.name, `is`(user1.name))
        assertThat(user2.password, `is`(user1.password))
    }
}