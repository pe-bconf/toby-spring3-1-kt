package toby.spring.user.service

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.PlatformTransactionManager
import toby.spring.user.dao.UserDao
import toby.spring.user.domain.Level
import toby.spring.user.domain.User
import toby.spring.user.service.UserServiceImpl.Companion.MIN_LOGCOUNT_FOR_SILVER
import toby.spring.user.service.UserServiceImpl.Companion.MIN_RECOMMEND_FOR_GOLD
import java.util.Arrays

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = ["/config/test-applicationContext.xml"])
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    private lateinit var userDao: UserDao
    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager
    @Autowired
    private lateinit var mailSender: MailSender

    private lateinit var users: List<User>

    companion object {
        class TestUserService(private val id: String?) : UserServiceImpl() {
            override fun upgradeLevel(user: User) {
                if (user.id == this.id) throw TestUserServiceException()
                super.upgradeLevel(user)
            }
        }

        class TestUserServiceException : RuntimeException()

        class MockMailSender: MailSender {
            private val requests: MutableList<String> = mutableListOf()

            fun getRequests(): List<String> {
                return this.requests
            }

            override fun send(mailMessage: SimpleMailMessage) {
                requests.add(mailMessage.getTo()[0])
            }

            override fun send(vararg mailMessages: SimpleMailMessage) {
            }
        }
    }

    @Before
    fun setUp() {
        this.users = Arrays.asList(
            User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "bumjin@ksug.org"), // 변화없음
            User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "joytouch@ksug.org"), // 변경예상
            User("erwins", "신승한", "p3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 10, MIN_RECOMMEND_FOR_GOLD - 1, "erwins@ksug.org"), // 변화없음
            User("madnite1", "이상호", "p4", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 10, MIN_RECOMMEND_FOR_GOLD, "madnite1@ksug.org"), // 변경예상
            User("green ", "오만규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "green@ksug.org") // 변화없음
        )
    }

    @Test
    fun upgradeLevels() {
        userDao.deleteAll()

        for (user in users) {
            userDao.add(user)
        }

        val mockMailSender = MockMailSender()
        userServiceImpl.setMailSender(mockMailSender)

        userService.upgradeLevels()

        checkLevelUpgraded(users[0], false)
        checkLevelUpgraded(users[1], true)
        checkLevelUpgraded(users[2], false)
        checkLevelUpgraded(users[3], true)
        checkLevelUpgraded(users[4], false)

        val requests = mockMailSender.getRequests()
        assertThat(requests.size, `is`(2))
        assertThat(requests[0], `is`(users[1].email))
        assertThat(requests[1], `is`(users[3].email))
    }

    private fun checkLevel(user: User, expectedLevel: Level) {
        val userUpdate = userDao.get(user.id)
        assertThat(userUpdate.level, `is`(expectedLevel))
    }

    private fun checkLevelUpgraded(user: User, upgraded: Boolean) {
        val userUpdate = userDao.get(user.id)

        if (upgraded) {
            assertThat(userUpdate.level, `is`(user.level?.nextLevel()))
        } else {
            assertThat(userUpdate.level, `is`(user.level))
        }
    }

    @Test
    fun add() {
        userDao.deleteAll()

        val userWithLevel = users.get(4)
        val userWithoutLevel = users.get(0)
        userWithoutLevel.level = null

        userService.add(userWithLevel)
        userService.add(userWithoutLevel)

        val userWithLevelRead = userDao.get(userWithLevel.id)
        val userWithoutLevelRead = userDao.get(userWithoutLevel.id)

        assertThat(userWithLevelRead.level, `is`(userWithLevel.level))
        assertThat(userWithoutLevelRead.level, `is`(Level.BASIC))
    }

    @Test
    fun upgradeAllOrNothing() {
        val testUserServiceImpl = TestUserService(users[3].id)
        testUserServiceImpl.setUserDao(this.userDao)
        testUserServiceImpl.setMailSender(this.mailSender)

        val testUserService = UserServiceTx()
        testUserService.setUserService(testUserServiceImpl)
        testUserService.setTransactionManager(this.transactionManager)

        userDao.deleteAll()
        for (user in users) userDao.add(user)

        try {
            testUserService.upgradeLevels()
            fail("TestUserServiceException expected")
        } catch (e: TestUserServiceException) {
            // Test exception caught
        }

        checkLevelUpgraded(users[1], false)
    }

}
