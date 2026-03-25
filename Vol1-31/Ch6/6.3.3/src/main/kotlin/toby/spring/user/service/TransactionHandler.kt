package toby.spring.user.service

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class TransactionHandler: InvocationHandler {
    private lateinit var target: Any
    private lateinit var transactionManger: PlatformTransactionManager
    private lateinit var pattern: String

    fun setTarget(target: Any) {
        this.target = target
    }

    fun setPattern(pattern: String) {
        this.pattern = pattern
    }

    fun setTransactionManger(transactionManger: PlatformTransactionManager) {
        this.transactionManger = transactionManger
    }

    private fun invokeInTransaction(method: Method, args: Array<Any>): Any {
        val status = this.transactionManger.getTransaction(DefaultTransactionDefinition())

        try {
            val ret = method.invoke(target, args)
            this.transactionManger.commit(status)
            return ret
        } catch (e: InvocationTargetException) {
            this.transactionManger.rollback(status)
            throw e.targetException
        }
    }

    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
       if (method.name.startsWith(pattern)) {
           return this.invokeInTransaction(method, args)
       } else {
           return method.invoke(target, *args)
       }
    }

}