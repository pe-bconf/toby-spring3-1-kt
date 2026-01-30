package toby.user.dao

import toby.account.AccountDao
import toby.message.MessageDao

class UserDaoFactory {
    fun connectionMaker(): ConnectionMaker = DConnectionMaker()

    fun userDao(): UserDao {
        val connectionMaker = connectionMaker()
        return UserDao(connectionMaker)
    }

    fun accountDao(): AccountDao = AccountDao(connectionMaker())
    fun messageDao(): MessageDao = MessageDao(connectionMaker())
}