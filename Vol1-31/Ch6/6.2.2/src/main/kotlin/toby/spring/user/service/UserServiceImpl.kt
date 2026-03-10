package toby.spring.user.service

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import toby.spring.user.dao.UserDao
import toby.spring.user.domain.Level
import toby.spring.user.domain.User

open class UserServiceImpl: UserService {
    private lateinit var userDao: UserDao

    companion object {
        const val MIN_LOGCOUNT_FOR_SILVER: Int = 50
        const val MIN_RECOMMEND_FOR_GOLD: Int = 30
    }

    fun setUserDao(userDao: UserDao) {
        this.userDao = userDao
    }

    fun canUpgradeLevel(user: User): Boolean {
        val currentLevel = user.level ?: throw IllegalStateException("No Level")

        return when (currentLevel) {
            Level.BASIC -> (user.login ?: 0) >= MIN_LOGCOUNT_FOR_SILVER
            Level.SILVER -> (user.recommend ?: 0) >= MIN_RECOMMEND_FOR_GOLD
            Level.GOLD -> false
        }
    }

    open fun upgradeLevel(user: User) {
        user.upgradeLevel()
        userDao.update(user)
        sendUpgradeEmail(user)
    }

    override fun upgradeLevels() {
        val users: List<User> = userDao.getAll()
        for (user in users) {
            if (this.canUpgradeLevel(user)) { // 갱신 필요 여부
                this.upgradeLevel(user) // 갱신 동작
            }
        }
    }

    override fun add(user: User) {
        if (user.level == null) {
            user.level = Level.BASIC
        }

        userDao.add(user)
    }

    private lateinit var mailSender: MailSender

    fun setMailSender(mailSender: MailSender) {
        this.mailSender = mailSender
    }

    private fun sendUpgradeEmail(user: User) {
        val mailMessage = SimpleMailMessage()
        mailMessage.setTo(user.email)
        mailMessage.setFrom("useradmin@ksug.org")
        mailMessage.setSubject("Upgrade 안내")

        mailMessage.setText(user.level?.name)
        //mailMessage.setText("사용자님의 등급이 ${user.level}로 업그레이드되었습니다.")

        mailSender.send(mailMessage)
    }

}