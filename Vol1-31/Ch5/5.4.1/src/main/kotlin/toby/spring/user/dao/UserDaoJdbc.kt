package toby.spring.user.dao

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import toby.spring.user.domain.Level
import toby.spring.user.domain.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class UserDaoJdbc: UserDao {
    private lateinit var jdbcTemplate: JdbcTemplate

    fun setDataSource(dataSource: DataSource) {
        this.jdbcTemplate = JdbcTemplate(dataSource)
    }

    override fun add(user: User): Int {
        return this.jdbcTemplate.update(
            "INSERT INTO USERS(id, name, password, `level`, login, recommend, email) VALUES (?, ?, ?, ?, ?, ?, ?)",
            user.id,
            user.name,
            user.password,
            user.level?.intValue(),
            user.login,
            user.recommend,
            user.email
        )
    }

    override fun deleteAll(): Int {
        return this.jdbcTemplate.update(
            object: PreparedStatementCreator {
                override fun createPreparedStatement(con: Connection): PreparedStatement {
                    return con.prepareStatement("delete from users")
                }
            }
        )
    }

    override fun getCount(): Int {
        return this.jdbcTemplate.queryForInt("select count(*) from users")
    }

    val userMapper = object: RowMapper<User> {
        override fun mapRow(rs: ResultSet, rowNum: Int): User {
            val user = User()
            user.id = rs.getString("id")
            user.name = rs.getString("name")
            user.password = rs.getString("password")
            user.level = Level.valueOf(rs.getInt("level"))
            user.login = rs.getInt("login")
            user.recommend = rs.getInt("recommend")
            user.email = rs.getString("email")
            return user
        }
    }

    override fun get(id: String?): User {
        return this.jdbcTemplate.queryForObject("select id, name, password, level, login, recommend, email from users where id = ?", arrayOf(id),
            userMapper)
    }

    override fun getAll(): List<User> {
        return this.jdbcTemplate.query("select * from users order by id",
            userMapper)
    }

    override fun update(user1: User): Int {
        return this.jdbcTemplate.update(
            "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ?",
            user1.name, user1.password, user1.level?.intValue(), user1.login, user1.recommend, user1.email, user1.id
        )
    }
}