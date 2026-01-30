package toby.user.dao

import toby.user.domain.User

val dbDriverName = "com.mysql.cj.jdbc.Driver"
val testTarget = "sql/"

var jdbcUrl = ""
val dbUser = "user"
val dbPassword = "pass"
val dbName = "testdb"

/**
 * 추상 클래스 내에서 컨테이너 실행 및
 * 테스트 함수 실행 제약으로 별도 생성
 */
class UserDaoTest {
    fun main() {
        // 실제 액션 위치
        val factory = UserDaoFactory()

        val dao = factory.userDao()
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