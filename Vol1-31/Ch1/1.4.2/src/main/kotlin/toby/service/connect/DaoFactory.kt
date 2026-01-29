package toby.service.connect

import toby.account.AccountDao
import toby.message.MessageDao
import toby.user.dao.UserDao

var JDBC_URL: String = ""
    get() = field
    set(jdbcUrl) {
        field = jdbcUrl
    }

class DaoFactory {
    fun connectionMaker(): ConnectionMaker = DConnectionMaker()

    fun userDao(): UserDao {
        val connectionMaker = connectionMaker()
        return UserDao(connectionMaker)
    }

    fun accountDao(): AccountDao = AccountDao(connectionMaker())
    fun messageDao(): MessageDao = MessageDao(connectionMaker())
}