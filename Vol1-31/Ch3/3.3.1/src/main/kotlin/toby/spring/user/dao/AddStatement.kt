package toby.spring.user.dao

import toby.spring.user.domain.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class AddStatement: StatementStrategy {
    var user: User

    constructor(user: User) {
        this.user = user
    }

    override fun makePreparedStatement(c: Connection): PreparedStatement {
        val stmt = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)")
        stmt.setString(1, user.id)
        stmt.setString(2, user.name)
        stmt.setString(3, user.password)
        return stmt
    }
}