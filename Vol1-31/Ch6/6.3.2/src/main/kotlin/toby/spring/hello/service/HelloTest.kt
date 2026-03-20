package toby.spring.hello.service

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class HelloTest {
    @Test
    fun simplyProxy() {
        val hello = HelloUppercase(HelloTarget())
        assertThat(hello.sayHello("Toby"), `is`("HELLO TOBY"))
        assertThat(hello.sayHi("Toby"), `is`("HI TOBY"))
        assertThat(hello.sayThankYou("Toby"), `is`("THANK YOU TOBY"))
    }
}