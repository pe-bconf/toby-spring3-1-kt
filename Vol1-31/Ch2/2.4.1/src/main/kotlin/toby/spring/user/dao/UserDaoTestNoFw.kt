package toby.spring.user.dao

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import toby.spring.user.domain.User


class UserDaoTestNoFw {
    private lateinit var dao: UserDao

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var user3: User

    @Before
    fun setUp() {
        // val dataSource = SingleConnectionDataSource(jdbcUrl, dbUser, dbPassword, true)
        dao = UserDao()
        val dataSource = SingleConnectionDataSource(jdbcUrl, dbUser, dbPassword, true)
        dao.setDataSource(dataSource)

        this.user1 = User("gyumee", "박성철", "springno1")
        this.user2 = User("leegw700", "이길원", "springno2")
        this.user3 = User("bumjin", "박범진", "springno3")
    }

    @Test
    fun addAndGet() {
        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.add(this.user1)
        dao.add(this.user2)
        assertThat(dao.getCount(), `is`(2))

        val userget1 = dao.get(this.user1.id)
        assertThat(userget1?.name, `is`(this.user1.name))
        assertThat(userget1?.password, `is`(this.user1.password))

        val userget2 = dao.get(this.user2.id)
        assertThat(userget2?.name, `is`(this.user2.name))
        assertThat(userget2?.password, `is`(this.user2.password))
    }

    @Test
    fun count() {
        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.add(this.user1)
        assertThat(dao.getCount(), `is`(1))

        dao.add(this.user2)
        assertThat(dao.getCount(), `is`(2))

        dao.add(this.user3)
        assertThat(dao.getCount(), `is`(3))
    }

    @Test(expected = EmptyResultDataAccessException::class)
    fun getUserFailure() {
        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.get("unknown_id");
    }
}