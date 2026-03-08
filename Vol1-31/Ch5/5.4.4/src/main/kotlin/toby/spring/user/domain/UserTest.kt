package toby.spring.user.domain

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

import toby.spring.user.domain.User

class UserTest {
    lateinit var user: User

    @Before
    fun setUp() {
        user = User()
    }

    @Test
    fun upgradeLevel() {
        val levels = Level.values()
        for (level in levels) {
            if (level.nextLevel() == null) continue
            user.level = level
            user.upgradeLevel()
            assertThat(user.level, `is`(level.nextLevel()))
        }
    }

    @Test(expected = IllegalStateException::class)
    fun cannotUpgradeLevel() {
        val levels = Level.values()
        for (level in levels) {
            if (level.nextLevel() != null) continue
            user.level = level
            user.upgradeLevel()
        }
    }
}