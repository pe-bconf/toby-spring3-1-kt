package toby.spring.user.dao

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import toby.spring.user.domain.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class UserDao {
    private lateinit var jdbcTemplate: JdbcTemplate

    fun setDataSource(dataSource: DataSource) {
        this.jdbcTemplate = JdbcTemplate(dataSource)
    }

    fun add(user: User) {
        this.jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",
            user.id,
            user.name,
            user.password)
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