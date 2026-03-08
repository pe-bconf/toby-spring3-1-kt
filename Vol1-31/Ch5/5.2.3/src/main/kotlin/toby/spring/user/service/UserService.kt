package toby.spring.user.service

import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.transaction.support.TransactionSynchronizationManager
import toby.spring.user.dao.UserDao
import toby.spring.user.domain.Level
import toby.spring.user.domain.User
import javax.sql.DataSource

open class UserService: UserLevelUpgradePolicy {
    private lateinit var userDao: UserDao

    /** 수동 주입 소스 */
    private lateinit var dataSource: DataSource

    fun setDataSource(dataSource: DataSource) {
        this.dataSource = dataSource
    }

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
        /*val users: List<User> = userDao.getAll()

        for (user in users) {
            if (this.canUpgradeLevel(user)) { // 갱신 필요 여부
                this.upgradeLevel(user) // 갱신 동작
            }
        }*/

        /** 수동 주입 소스 */
        TransactionSynchronizationManager.initSynchronization()
        val c = DataSourceUtils.getConnection(this.dataSource)
        c.autoCommit = false

        try {
            val users: List<User> = userDao.getAll()
            for (user in users) {
                if (this.canUpgradeLevel(user)) { // 갱신 필요 여부
                    this.upgradeLevel(user) // 갱신 동작
                }
            }

            c.commit()
        } catch (ex: Exception) {
            c.rollback()
            throw ex
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource)
            TransactionSynchronizationManager.unbindResource(this.dataSource)
            TransactionSynchronizationManager.clearSynchronization()
        }
    }

    fun add(user: User) {
        if (user.level == null) {
            user.level = Level.BASIC
        }

        userDao.add(user)
    }
}