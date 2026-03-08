package toby.spring.user.dao

import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import org.springframework.jdbc.support.SQLExceptionTranslator
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import toby.spring.user.domain.Level
import toby.spring.user.domain.User
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.sql.DataSource

val testTarget = "sql/"
val dbUser = "user"
val dbPassword = "testpass"
val dbName = "testdb"

var jdbcUrl = "jdbc:mysql://localhost:3306/$dbName?characterEncoding=UTF-8"

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = ["/config/test-applicationContext.xml"])
class UserDaoTest {
    @Autowired
    private lateinit var ctx: ApplicationContext
    @Autowired
    private lateinit var dao: UserDao

    @Autowired
    private lateinit var dataSource: DataSource

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var user3: User
    private var stmt: PreparedStatement? = null

    @Before
    fun setUp() {
        this.user1 = User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0)
        this.user2 = User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10)
        this.user3 = User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40)
    }

    @Test
    fun addAndGet() {
        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.add(this.user1)
        dao.add(this.user2)
        assertThat(dao.getCount(), `is`(2))

        val userget1 = dao.get(this.user1.id)
        checkSameUser(userget1, this.user1)

        val userget2 = dao.get(this.user2.id)
        checkSameUser(userget2, this.user2)
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

    @Test
    fun getAll() {
        dao.deleteAll()

        dao.add(this.user1)
        val users1 = dao.getAll()
        assertThat(users1.size, `is`(1))
        checkSameUser(user1, users1.get(0))

        dao.add(this.user2)
        val users2 = dao.getAll()
        assertThat(users2.size, `is`(2))
        checkSameUser(user1, users2.get(0))
        checkSameUser(user2, users2.get(1))

        dao.add(this.user3)
        val users3 = dao.getAll()
        assertThat(users3.size, `is`(3))
        checkSameUser(user3, users3.get(0))
        checkSameUser(user1, users3.get(1))
        checkSameUser(user2, users3.get(2))
    }

    fun checkSameUser(target: User, source: User) {
        assertThat(target.id, `is`(source.id))
        assertThat(target.name, `is`(source.name))
        assertThat(target.password, `is`(source.password))
        assertThat(target.level, `is`(source.level))
        assertThat(target.login, `is`(source.login))
        assertThat(target.recommend, `is`(source.recommend))
    }

    @Test(expected = DataAccessException::class)
    fun addUserFailure() {
        dao.deleteAll()
        assertThat(dao.getCount(), `is`(0))

        dao.add(this.user1)
        dao.add(this.user2)
        dao.add(this.user2)
    }

    @Test(expected = DuplicateKeyException::class)
    fun duplicateKey() {
        dao.deleteAll()

        dao.add(this.user1)
        dao.add(this.user1)
    }

    @Test
    fun sqlExceptionTranslator() {
        dao.deleteAll()

        try {
            dao.add(this.user1)
            dao.add(this.user1)
        } catch (ex: DuplicateKeyException) {
            val sqlEx : SQLException = ex.rootCause as SQLException
            val set : SQLExceptionTranslator = SQLErrorCodeSQLExceptionTranslator(this.dataSource)

            assertThat(set.translate(null, null, sqlEx), `is`(DuplicateKeyException::class.java))
        }
    }

    @After
    fun after() {
        dao.deleteAll()
    }

    @Test
    fun update() {
        dao.deleteAll()

        dao.add(this.user1)
        dao.add(this.user2)

        user1.name = "오민규"
        user1.password = "springno6"
        user1.level = Level.GOLD
        user1.login = 1000
        user1.recommend = 999

        dao.update(user1)

        val user1update = dao.get(this.user1.id)
        checkSameUser(user1, user1update)
        val user2same = dao.get(this.user2.id)
        checkSameUser(this.user2, user2same)
    }

}