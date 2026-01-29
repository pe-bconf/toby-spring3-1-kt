
import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import toby.user.domain.User
import java.sql.DriverManager

val testTarget = "sql/"
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
            withInitScript("$testTargetinit.sql")
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

        val user = User(1, "test11", "test11")
        println(user)
    }
}
