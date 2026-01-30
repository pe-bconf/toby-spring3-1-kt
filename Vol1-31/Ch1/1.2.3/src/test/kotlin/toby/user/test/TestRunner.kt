package toby.user.test
import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import sun.net.ftp.FtpClient.defaultPort
import toby.user.dao.NUserDao
import toby.user.dao.UserDao
import toby.user.dao.dbName
import toby.user.dao.dbPassword
import toby.user.dao.dbUser
import toby.user.dao.jdbcUrl
import toby.user.dao.testTarget
import toby.user.domain.User
import java.sql.DriverManager

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

        // 실제 액션 위치
        val dao = NUserDao()

        val user = User()
        user.id = "tester"
        user.name = "테스터"
        user.password = "test"

        dao.add(user)

        println("${user.id} 등록 성공")

        val user2 = dao.get(user.id)
        println("이름: ${user2.name}, 비밀번호: ${user2.password}")
        println("${user2.id} 조회 성공")
    }
}
