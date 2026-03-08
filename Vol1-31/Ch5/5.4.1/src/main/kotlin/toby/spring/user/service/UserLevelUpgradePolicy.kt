package toby.spring.user.service

import toby.spring.user.domain.User

interface UserLevelUpgradePolicy {
    fun canUpgradeLevel(user: User): Boolean
    fun upgradeLevel(user: User)
}