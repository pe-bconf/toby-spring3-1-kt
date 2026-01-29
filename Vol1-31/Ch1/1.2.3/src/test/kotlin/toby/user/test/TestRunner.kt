package toby.user.test
import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import sun.net.ftp.FtpClient.defaultPort
import toby.user.dao.NUserDao
import toby.user.dao.UserDao
import toby.user.domain.User
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

        // 실제 액션 위치
        val dao = NUserDao()

        // testcontainer 주소 유동성으로 인해 강제 설정
        toby.user.dao.JDBC_URL = jdbcUrl

        val user = User()
        user.id = "tester"
        user.name = "테스터"
        user.password = "test"

        dao.addUser(user)

        println("${user.id} 등록성공")

        val user2 = dao.getUser(user.id)
        println("이름: ${user2?.name}, 비밀번호: ${user2?.password}")
        println("${user2?.id} 조회 성공")
    }
}
