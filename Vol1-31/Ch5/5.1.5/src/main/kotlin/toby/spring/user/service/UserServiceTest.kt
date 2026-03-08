package toby.spring.user.service

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import toby.spring.user.dao.UserDao
import toby.spring.user.domain.Level
import toby.spring.user.domain.User
import toby.spring.user.service.UserService.Companion.MIN_LOGCOUNT_FOR_SILVER
import toby.spring.user.service.UserService.Companion.MIN_RECOMMEND_FOR_GOLD
import java.util.Arrays

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = ["/config/test-applicationContext.xml"])
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var userDao: UserDao

    private lateinit var users: List<User>

    @Before
    fun setUp() {
        this.users = Arrays.asList(
            User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0), // 변화없음
            User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0), // 변경예상
            User("erwins", "신승한", "p3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 10, MIN_RECOMMEND_FOR_GOLD - 1), // 변화없음
            User("madnite1", "이상호", "p4", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 10, MIN_RECOMMEND_FOR_GOLD), // 변경예상
            User("green ", "오만규", "p5", Level.GOLD, 100, Integer.MAX_VALUE) // 변화없음
        )
    }

    @Test
    fun upgradeLevel() {
        userDao.deleteAll()

        for (user in users) {
            userDao.add(user)
        }

        userService.upgradeLevels()

        checkLevelUpgraded(users[0], false)
        checkLevelUpgraded(users[1], true)
        checkLevelUpgraded(users[2], false)
        checkLevelUpgraded(users[3], true)
        checkLevelUpgraded(users[4], false)


        /*checkLevel(users.get(0), Level.BASIC)
        checkLevel(users.get(1), Level.SILVER)
        checkLevel(users.get(2), Level.SILVER)
        checkLevel(users.get(3), Level.GOLD)
        checkLevel(users.get(4), Level.GOLD)*/
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

}
