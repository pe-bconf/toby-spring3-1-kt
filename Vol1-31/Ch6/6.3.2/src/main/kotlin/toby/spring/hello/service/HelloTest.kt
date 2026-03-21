package toby.spring.hello.service

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.lang.reflect.Proxy

class HelloTest {
    @Test
    fun simplyProxy() {
        // val hello = HelloUppercase(HelloTarget())
        val proxiedHello:Hello = Proxy.newProxyInstance(
            Hello::class.java.classLoader,
            arrayOf<Class<*>>(Hello::class.java),
            UppercaseHandler(HelloTarget())) as Hello
        assertThat(proxiedHello.sayHello("Toby"), `is`("HELLO TOBY"))
        assertThat(proxiedHello.sayHi("Toby"), `is`("HI TOBY"))
        assertThat(proxiedHello.sayThankYou("Toby"), `is`("THANK YOU TOBY"))
    }
}