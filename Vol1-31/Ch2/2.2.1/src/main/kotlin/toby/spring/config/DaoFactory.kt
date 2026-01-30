package toby.spring.config

import com.mysql.cj.jdbc.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import toby.spring.dao.UserDao
import javax.sql.DataSource

var JDBC_URL: String = ""
    get() = field
    set(jdbcUrl) {
        field = jdbcUrl
    }

@Configuration
open class DaoFactory {
    @Bean
    open fun dataSource(): DataSource {
        val dataSource = SimpleDriverDataSource()
        dataSource.setDriverClass(Driver::class.java)
        dataSource.url = JDBC_URL
        dataSource.username = "user"
        dataSource.password = "pass"

        return dataSource
    }

    @Bean
    open fun userDao(): UserDao {
        val userDao = UserDao()
        userDao.setDataSource(dataSource())
        return userDao
    }
}