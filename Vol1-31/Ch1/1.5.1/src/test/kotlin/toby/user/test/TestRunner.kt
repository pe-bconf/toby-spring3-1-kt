package toby.user.test
import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import toby.spring.config.DaoFactory
import toby.spring.context.MainContext
import toby.spring.domain.User
import java.sql.DriverManager

val testTarget = "sql/"
var defaultPort = 3306
val dbName = "testdb"
val dbUser = "user"
val dbPassword = "pass"

@Testcontainers
class TestRunner {
    companion object {
        @Container
        private val mySQLContainer = MySQLContainer<Nothing>("mysql:8.0").apply {
            withDatabaseName(dbName)
            withUsername(dbUser)
            withPassword(dbPassword)
            withInitScript("${testTarget}users_create.sql")
        }
    }

    @Test
    fun test() {
        val jdbcUrl = mySQLContainer.jdbcUrl

        DriverManager.getConnection(jdbcUrl, dbUser, dbPassword).use {
            conn ->
            val rs = conn.createStatement().executeQuery("SHOW TABLES")
            while (rs.next()) {
                println("등록된 테이블 이름: ${rs.getString(1)}")
            }
        }

        toby.spring.config.JDBC_URL = jdbcUrl

        // 실제 액션 위치
        val mainRunner = MainContext()
        mainRunner.testMain()
    }
}
