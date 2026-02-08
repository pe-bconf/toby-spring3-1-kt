package toby.spring.user.dao

import org.springframework.dao.EmptyResultDataAccessException
import toby.spring.user.domain.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

class UserDao {
    private lateinit var dataSource: DataSource

    private lateinit var jdbcContext: JdbcContext

    constructor() {}

    fun setDataSource(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    fun setJdbcContext(jdbcContext: JdbcContext) {
        this.jdbcContext = jdbcContext
    }

    fun add(user: User) {
        this.jdbcContext.workWithStatementStrategy(object: StatementStrategy {
            override fun makePreparedStatement(c: Connection): PreparedStatement {
                val stmt = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
                stmt.setString(1, user.id)
                stmt.setString(2, user.name)
                stmt.setString(3, user.password)
                return stmt
            }
        })
    }

    fun get(id: String?): User {
        var c: Connection? = null
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
        }
    }

    fun deleteAll() {
        this.jdbcContext.workWithStatementStrategy(object: StatementStrategy {
                override fun makePreparedStatement(c: Connection): PreparedStatement {
                    val stmt = c.prepareStatement("delete from users")
                    return stmt
                }
        })
    }

    fun getCount(): Int {
        var c: Connection? = null
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
        }
    }
}