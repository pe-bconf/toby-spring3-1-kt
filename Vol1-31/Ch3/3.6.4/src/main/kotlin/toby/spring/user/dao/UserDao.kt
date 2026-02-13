package toby.spring.user.dao

import org.springframework.dao.EmptyResultDataAccessException
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
    private lateinit var dataSource: DataSource
    private lateinit var jdbcTemplate: JdbcTemplate

    fun setDataSource(dataSource: DataSource) {
        this.jdbcTemplate = JdbcTemplate(dataSource)
        this.dataSource = dataSource
    }

    fun add(user: User) {
        /*this.jdbcContext.executeSql("insert into users(id, name, password) values (?, ?, ?)",
    user.id,
            user.name,
            user.password
        )*/
        this.jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)",
            user.id,
            user.name,
            user.password)
    }

    fun get(id: String?): User {
        /*var c: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            c = dataSource.connection
            ps = c.prepareStatement("select id, name, password from users where id = ?")
            ps.setString(1, id)
            rs = ps.executeQuery()

            var user: User? = null
            if (rs.next()) {
                user = User()
                user.id = rs.getString("id")
                user.name = rs.getString("name")
                user.password = rs.getString("password")
            }

            if (user == null) {
                throw EmptyResultDataAccessException(1)
            }

            return user
        } catch (e: SQLException) {
            throw e
        } finally {
            if (rs != null) {
                try {
                    rs.close()
                } catch (e: SQLException) {}
            }

            if (ps != null) {
                try {
                    ps.close()
                } catch (e: SQLException) {}
            }

            if (c != null) {
                try {
                    c.close()
                } catch (e: SQLException) {}
            }
        }*/
        return this.jdbcTemplate.queryForObject("select id, name, password from users where id = ?", arrayOf(id),
            object: RowMapper<User> {
                override fun mapRow(rs: ResultSet, rowNum: Int): User {
                    val user = User()
                    user.id = rs.getString("id")
                    user.name = rs.getString("name")
                    user.password = rs.getString("password")
                    return user
                }
            })
    }

    fun getCount(): Int {
        /*var c: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            c = dataSource.connection
            ps = c.prepareStatement("select count(*) from users")
            rs = ps.executeQuery()
            rs.next()
            return rs.getInt(1)
        } catch (e: SQLException) {
            throw e
        } finally {
            if (rs != null) {
                try {
                    rs.close()
                } catch (e: SQLException) {}
            }

            if (ps != null) {
                try {
                    ps.close()
                } catch (e: SQLException) {}
            }

            if (c != null) {
                try {
                    c.close()
                } catch (e: SQLException) {}
            }
        }*/
        return this.jdbcTemplate.queryForInt("select count(*) from users")
    }

    fun deleteAll() {
        // this.jdbcContext.executeSql("delete from users");
        this.jdbcTemplate.update(
            object: PreparedStatementCreator {
                override fun createPreparedStatement(con: Connection): PreparedStatement {
                    return con.prepareStatement("delete from users")
                }
            }
        )
    }

    fun getAll(): List<User> {
        return this.jdbcTemplate.query("select * from users order by id",
            object: RowMapper<User> {
                override fun mapRow(rs: ResultSet?, rowNum: Int): User? {
                    val user = User()
                    user.id = rs?.getString("id")
                    user.name = rs?.getString("name")
                    user.password = rs?.getString("password")
                    return user
                }
            })
    }

}