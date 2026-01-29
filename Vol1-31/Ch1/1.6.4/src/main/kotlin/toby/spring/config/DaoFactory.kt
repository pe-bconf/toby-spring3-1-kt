package toby.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import toby.spring.connect.ConnectionMaker
import toby.spring.connect.DConnectionMaker
import toby.spring.dao.UserDao

var JDBC_URL: String = ""
    get() = field
    set(jdbcUrl) {
        field = jdbcUrl
    }

@Configuration
open class DaoFactory {
    @Bean
    open fun connectionMaker(): ConnectionMaker = DConnectionMaker()

    @Bean
    open fun userDao(): UserDao = UserDao(connectionMaker())
}