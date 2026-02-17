package toby.spring.user.dao

import toby.spring.user.domain.User

interface UserDao {
    fun getCount(): Int
    fun get(id: String?): User
    fun getAll(): List<User>

    fun add(user: User)
    fun deleteAll()
}