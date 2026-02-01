package toby.spring.user.dao

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.dao.EmptyResultDataAccessException
import toby.spring.user.domain.User

val testTarget = "sql/"
val dbUser = "user"
val dbPassword = "pass"
val dbName = "testdb"

var jdbcUrl = "jdbc:mysql://localhost:3306/springbook?characterEncoding=UTF-8"

class UserDaoTest {
    @Test
    fun addAndGet() {
        val ctx = GenericXmlApplicationContext("config/applicationContext.xml")

        val dao = ctx.getBean("userDao", UserDao::class.java)

        val user1 = User("gyumee", "박성철", "springno1")
        val user2 = User("leegw700", "이길원", "springno2")

        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.add(user1)
        dao.add(user2)
        assertThat(dao.getCount(), `is`(2))

        val userget1 = dao.get(user1?.id)
        assertThat(userget1?.name, `is`(user1.name))
        assertThat(userget1?.password, `is`(user1.password))

        val userget2 = dao.get(user2?.id)
        assertThat(userget2?.name, `is`(user2.name))
        assertThat(userget2?.password, `is`(user2.password))
    }

    @Test
    fun count() {
        val ctx = GenericXmlApplicationContext("config/applicationContext.xml")
        val dao = ctx.getBean("userDao", UserDao::class.java)

        val user1 = User("gyumee", "박성철", "springno1")
        val user2 = User("leegw700", "이길원", "springno2")
        val user3 = User("bumjin", "박범진", "springno3")

        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.add(user1)
        assertThat(dao.getCount(), `is`(1))

        dao.add(user2)
        assertThat(dao.getCount(), `is`(2))

        dao.add(user3)
        assertThat(dao.getCount(), `is`(3))
    }

    @Test(expected = EmptyResultDataAccessException::class)
    fun getUserFailure() {
        val ctx = GenericXmlApplicationContext("config/applicationContext.xml")
        val dao = ctx.getBean("userDao", UserDao::class.java)

        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.get("unknown_id");
    }
}