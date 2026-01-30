package toby.spring.context

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.context.support.GenericXmlApplicationContext
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import toby.spring.config.JDBC_URL
import toby.spring.dao.UserDao
import toby.spring.domain.User
import java.sql.DriverManager
import kotlin.use

const val testTarget = "sql/"
const val dbName = "testdb"
const val dbUser = "user"
const val dbPassword = "pass"

@Testcontainers
class UserDaoTest {
    companion object {
        @Container
        private val mySQLContainer = MySQLContainer<Nothing>("mysql:8.0").apply {
            withDatabaseName(dbName)
            withUsername(dbUser)
            withPassword(dbPassword)
            withInitScript("${testTarget}users_create.sql")
        }
    }

    @BeforeEach
    fun beforeClass(): Unit {
        val jdbcUrl = mySQLContainer.jdbcUrl

        DriverManager.getConnection(jdbcUrl, dbUser, dbPassword).use { conn ->
            val rs = conn.createStatement().executeQuery("SHOW TABLES")
            while (rs.next()) {
                println("등록된 테이블 이름: ${rs.getString(1)}")
            }
        }

        JDBC_URL = jdbcUrl
    }

    @Test
    fun addAndGet() {
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