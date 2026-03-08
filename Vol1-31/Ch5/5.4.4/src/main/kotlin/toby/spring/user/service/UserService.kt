package toby.spring.user.service

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import toby.spring.user.dao.UserDao
import toby.spring.user.domain.Level
import toby.spring.user.domain.User
import java.io.UnsupportedEncodingException
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

open class UserService: UserLevelUpgradePolicy {
    private lateinit var userDao: UserDao

    private lateinit var transactionManager: PlatformTransactionManager

    fun setTransactionManager(transactionManager: PlatformTransactionManager) {
        this.transactionManager = transactionManager
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
        sendUpgradeEmail(user)
    }

    fun upgradeLevels() {
        val status = this.transactionManager.getTransaction(DefaultTransactionDefinition())

        try {
            val users: List<User> = userDao.getAll()
            for (user in users) {
                if (this.canUpgradeLevel(user)) { // 갱신 필요 여부
                    this.upgradeLevel(user) // 갱신 동작
                }
            }

            this.transactionManager.commit(status)
        } catch (ex: Exception) {
            this.transactionManager.rollback(status)
            throw ex
        }
    }

    fun add(user: User) {
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