package toby.spring.user.service

import org.eclipse.core.internal.runtime.PerformanceStatsProcessor.changed
import toby.spring.user.dao.UserDao
import toby.spring.user.domain.Level
import toby.spring.user.domain.User

class UserService {
    private lateinit var userDao: UserDao

    fun setUserDao(userDao: UserDao) {
        this.userDao = userDao
    }

    fun upgradeLevels() {
        val users: List<User> = userDao.getAll()

        for (user in users) {
            var changed: Boolean? = null

            if (user.level == Level.BASIC && user.login!! >= 50) {
                user.level = Level.SILVER
                changed = true
            } else if (user.level == Level.SILVER && user.recommend!! >= 30) {
                user.level = Level.GOLD
                changed = true
            } else if (user.level == Level.GOLD) {
                changed = false
            } else {
                changed = false
            }

            if (changed) {
                this.userDao.update(user)
            }
        }
    }
}