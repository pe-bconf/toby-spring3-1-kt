package toby.spring.user.dao

var JDBC_URL: String = ""
    get() = field
    set(jdbcUrl) {
        field = jdbcUrl
    }

// @Configuration
open class DaoFactory {
    // @Bean
    open fun connectionMaker(): ConnectionMaker = DConnectionMaker()

    // @Bean
    open fun userDao(): UserDao {
        val userDao = UserDao()
        userDao.setConnectionMaker(connectionMaker())
        return userDao
    }
}