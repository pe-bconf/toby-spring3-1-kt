package toby.spring.user.dao

import java.sql.Connection
import java.sql.PreparedStatement

interface StatementStrategy {
    fun makePreparedStatement(c: Connection): PreparedStatement
}