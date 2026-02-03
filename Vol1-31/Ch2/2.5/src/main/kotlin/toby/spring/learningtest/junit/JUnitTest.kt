package toby.spring.learningtest.junit

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.matchers.JUnitMatchers.either
import org.junit.matchers.JUnitMatchers.hasItem
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

// 참고 org.hamcrest.CoreMatchers.`is`와 org.hamcrest.core.Is.`is`는 같은 동작이다
// org.hamcrest.CoreMatchers.`is` 함수가 org.hamcrest.core.Is.`is` 함수를 사용한다
@RunWith(SpringJUnit4ClassRunner::class)
// 책에선 누락된 로케이션 값 추가
@ContextConfiguration("classpath:config/junit.xml")
class JUnitTest() {
    @Autowired
    lateinit var context: ApplicationContext

    companion object {
        val testObjects: MutableSet<JUnitTest> = HashSet()
        var contextObject: ApplicationContext? = null
    }

    @Test
    fun test1() {
        assertThat(testObjects, not(hasItem(this)))
        testObjects.add(this)
        assertThat(contextObject == null || contextObject == this.context, `is`(true))
        contextObject = this.context
    }

    @Test
    fun test2() {
        assertThat(testObjects, not(hasItem(this)))
        testObjects.add(this)

        assertTrue(contextObject == null || contextObject == this.context)
        contextObject = this.context
    }

    @Test
    fun test3() {
        assertThat(testObjects, not(hasItem(this)))
        testObjects.add(this)

        assertThat(contextObject,
            either(`is`(nullValue(ApplicationContext::class.java)))
               .or(`is`(this.context))
        )
        contextObject = this.context
    }
}