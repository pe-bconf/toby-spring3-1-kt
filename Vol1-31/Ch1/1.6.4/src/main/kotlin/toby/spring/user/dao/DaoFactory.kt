package toby.spring.user.dao

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DaoFactory {
    @Bean
    open fun connectionMaker(): ConnectionMaker = DConnectionMaker()

    @Bean
    open fun userDao(): UserDao = UserDao(connectionMaker())
}