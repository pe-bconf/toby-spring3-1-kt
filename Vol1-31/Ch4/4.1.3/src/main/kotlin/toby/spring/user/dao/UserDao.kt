package toby.spring.user.dao

import com.mysql.cj.exceptions.MysqlErrorNumbers
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import toby.spring.user.domain.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

class UserDao {
    private lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var jdbcContext: JdbcContext

    fun setDataSource(dataSource: DataSource) {
        this.jdbcTemplate = JdbcTemplate(dataSource)
    }

    fun setJdbcContext(jdbcContext: JdbcContext) {
        this.jdbcContext = jdbcContext
    }

    @Throws(SQLException::class, )
    fun add(user: User) {
        try {
            this.jdbcTemplate.update(
                "insert into users(id, name, password) values (?, ?, ?)",
                user.id,
                user.name,
                user.password
            )
        } catch (e: SQLException) {
            if (e.errorCode == MysqlErrorNumbers.ER_DUP_ENTRY) {
                throw DuplicateUserIdException(e)
            } else {
                throw e
            }
        }
    }

    @Throws(RuntimeException::class, DuplicateUserIdException::class)
    fun addWithJdbcContext(user: User) {
        try {
            this.jdbcContext.workWithStatementStrategy(
                object: StatementStrategy {
                    override fun makePreparedStatement(c: Connection): PreparedStatement {
                        val stmt = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
                        stmt.setString(1, user.id)
                        stmt.setString(2, user.name)
                        stmt.setString(3, user.password)
                        return stmt
                    }
                }
            )
        } catch (e: SQLException) {
            if (e.errorCode == MysqlErrorNumbers.ER_DUP_ENTRY) {
                throw DuplicateUserIdException(e)
            } else {
                throw RuntimeException(e)
            }
        }
    }

    fun deleteAll() {
        this.jdbcTemplate.update(
            object: PreparedStatementCreator {
                override fun createPreparedStatement(con: Connection): PreparedStatement {
                    return con.prepareStatement("delete from users")
                }
            }
        )
    }

    fun getCount(): Int {
        return this.jdbcTemplate.queryForInt("select count(*) from users")
    }

    val userMapper = object: RowMapper<User> {
        override fun mapRow(rs: ResultSet, rowNum: Int): User {
            val user = User()
            user.id = rs.getString("id")
            user.name = rs.getString("name")
            user.password = rs.getString("password")
            return user
        }
    }

    fun get(id: String?): User {
        return this.jdbcTemplate.queryForObject("select id, name, password from users where id = ?", arrayOf(id),
            userMapper)
    }

    fun getAll(): List<User> {
        return this.jdbcTemplate.query("select * from users order by id",
            userMapper)
    }

}