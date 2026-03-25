package toby.spring.user.dao

import org.springframework.jdbc.core.JdbcTemplate
import toby.spring.user.domain.User
import javax.sql.DataSource

class MockUserDao(private val _users: List<User>): UserDao {
    private lateinit var jdbcTemplate: JdbcTemplate

    fun setDataSource(dataSource: DataSource) {
        this.jdbcTemplate = JdbcTemplate(dataSource)
    }

    private val users: List<User> = _users

    override fun getAll(): List<User> {
        return this.users
    }

    private val updated = ArrayList<User>()

    override fun update(user: User) {
        this.updated.add(user)
    }

    fun getUpdated(): List<User> {
        return this.updated
    }

    override fun add(user: User): Int { throw UnsupportedOperationException() }
    override fun deleteAll(): Int { throw UnsupportedOperationException() }
    override fun getCount(): Int { throw UnsupportedOperationException() }
    override fun get(id: String?): User { throw UnsupportedOperationException() }
}