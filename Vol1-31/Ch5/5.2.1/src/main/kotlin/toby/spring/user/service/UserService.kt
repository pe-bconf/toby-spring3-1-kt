package toby.spring.user.service

import toby.spring.user.dao.UserDao
import toby.spring.user.domain.Level
import toby.spring.user.domain.User

open class UserService: UserLevelUpgradePolicy {
    private lateinit var userDao: UserDao

    companion object {
        const val MIN_LOGCOUNT_FOR_SILVER: Int = 50
        const val MIN_RECOMMEND_FOR_GOLD: Int = 30
    }

    fun setUserDao(userDao: UserDao) {
        this.userDao = userDao
    }

    override fun canUpgradeLevel(user: User): Boolean {
        val currentLevel = user.level ?: throw IllegalStateException("No Level")

        return when (currentLevel) {
            Level.BASIC -> (user.login ?: 0) >= MIN_LOGCOUNT_FOR_SILVER
            Level.SILVER -> (user.recommend ?: 0) >= MIN_RECOMMEND_FOR_GOLD
            Level.GOLD -> false
        }
    }

    override fun upgradeLevel(user: User) {
        user.upgradeLevel()
        userDao.update(user)
    }

    fun upgradeLevels() {
        val users: List<User> = userDao.getAll()

        for (user in users) {
            if (this.canUpgradeLevel(user)) { // 갱신 필요 여부
                this.upgradeLevel(user) // 갱신 동작
            }
        }
    }

    fun add(user: User) {
        if (user.level == null) {
            user.level = Level.BASIC
        }

        userDao.add(user)
    }
}