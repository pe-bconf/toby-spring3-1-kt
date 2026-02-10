package toby.spring.user.dao

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.sql.DataSource

class JdbcContext {
    private lateinit var dataSource: DataSource

    fun setDataSource(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    fun workWithStatementStrategy(stmt: StatementStrategy) {
        var c: Connection? = null
        var ps: PreparedStatement? = null

        try {
            c = this.dataSource.connection
            ps = stmt.makePreparedStatement(c)
            ps.executeUpdate()
        } catch (e: SQLException) {
            throw e
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (e: SQLException) {}
            }

            if (c != null) {
                try {
                    c.close();
                } catch (e: SQLException) {}
            }
        }
    }

    fun executeSql(query: String, vararg params: String?) {
        this.workWithStatementStrategy(object: StatementStrategy {
            override fun makePreparedStatement(c: Connection): PreparedStatement {
                val stmt = c.prepareStatement(query)
                if (params.isNotEmpty()) {
                    for (i: Int in 1..params.size) {
                        stmt.setString(i, params[i-1].toString())
                    }
                }
                return stmt
            }
        })
    }
}