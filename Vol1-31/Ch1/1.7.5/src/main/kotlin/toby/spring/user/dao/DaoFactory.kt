package toby.spring.user.dao

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DaoFactory {
    @Bean
    fun connectionMaker(): ConnectionMaker = DConnectionMaker()

    @Bean
    fun userDao(): UserDao {
        val userDao = UserDao()
        userDao.setConnectionMaker(connectionMaker())
        return userDao
    }
}