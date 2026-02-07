package toby.spring.user.dao

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class DeleteAllStatement: StatementStrategy {
    override fun makePreparedStatement(c: Connection): PreparedStatement {
        val stmt = c.prepareStatement("delete from users")
        return stmt
    }
}