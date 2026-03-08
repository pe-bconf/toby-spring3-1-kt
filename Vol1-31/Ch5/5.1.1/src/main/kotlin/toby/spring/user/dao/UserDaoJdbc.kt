package toby.spring.user.dao

import toby.spring.user.dao.UserDao
import org.springframework.dao.DuplicateKeyException
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

    override fun add(user: User) {
        this.jdbcTemplate.update(
            "INSERT INTO USERS(id, name, password, `level`, login, recommend) VALUES (?, ?, ?, ?, ?, ?)",
            user.id,
            user.name,
            user.password,
            user.level?.intValue(),
            user.login,
            user.recommend
        )
    }

    override fun deleteAll() {
        this.jdbcTemplate.update(
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
            return user
        }
    }

    override fun get(id: String?): User {
        return this.jdbcTemplate.queryForObject("select id, name, password from users where id = ?", arrayOf(id),
            userMapper)
    }

    override fun getAll(): List<User> {
        return this.jdbcTemplate.query("select * from users order by id",
            userMapper)
    }

}