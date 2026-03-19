package toby.spring.reflection.service

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ReflectionTest {
    @Test
    fun invokeMethod() {
        val name: String = "Spring"

        // length()
        assertThat(name.length, `is`(6))

        val lengthMethod = String::class.java.getMethod("length")
        assertThat(lengthMethod.invoke(name), `is`(6))

        // charAt()
        assertThat(name[0], `is`('S'))

        val charAtMethod = String::class.java.getMethod("charAt", Int::class.javaPrimitiveType)
        assertThat(charAtMethod.invoke(name, 0), `is`('S'))

    }
}