import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import toby.user.dao.UserDaoTest
import toby.user.dao.dbName
import toby.user.dao.dbPassword
import toby.user.dao.dbUser
import toby.user.dao.jdbcUrl
import toby.user.dao.testTarget

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
        jdbcUrl = mySQLContainer.jdbcUrl

        UserDaoTest().main()
    }
}