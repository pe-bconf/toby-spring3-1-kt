package toby.spring.user.dao

import com.mysql.cj.jdbc.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import javax.sql.DataSource

@Configuration
class DaoFactory {
    @Bean
    fun dataSource(): DataSource {
        val dataSource = SimpleDriverDataSource()
        dataSource.setDriverClass(Driver::class.java)
        dataSource.url = jdbcUrl
        dataSource.username = "user"
        dataSource.password = "pass"

        return dataSource
    }

    @Bean
    fun userDao(): UserDao {
        val userDao = UserDao()
        userDao.setDataSource(dataSource())
        return userDao
    }
}