package toby.spring.user.service

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import toby.spring.user.domain.User

open class UserServiceTx: UserService {
    private lateinit var userService: UserService

    fun setUserService(userService: UserService) {
        this.userService = userService
    }

    private lateinit var transactionManager: PlatformTransactionManager

    fun setTransactionManager(transactionManager: PlatformTransactionManager) {
        this.transactionManager = transactionManager
    }

    override fun upgradeLevels() {
        val status = this.transactionManager.getTransaction(DefaultTransactionDefinition())

        try {
            this.userService.upgradeLevels()
            this.transactionManager.commit(status)
        } catch (ex: RuntimeException) {
            this.transactionManager.rollback(status)
            throw ex
        }
    }

    override fun add(user: User) {
        this.userService.add(user)
    }
}