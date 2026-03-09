package toby.spring.user.service

import toby.spring.user.domain.User

interface UserService {
    fun upgradeLevels()
    fun add(user: User)
}