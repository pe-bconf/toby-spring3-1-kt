package springbook.learntest.template

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CalcSumTest {
    lateinit var calculator: Calculator
    lateinit var numFilepath: String

    @Before
    fun setUp() {
        calculator = Calculator()
        numFilepath = this::class.java.getResource("/numbers").getPath()
    }

    @Test
    fun sumOfNumber() {
        assertThat(this.calculator.calcSum(numFilepath), `is`(10))
    }

    @Test
    fun timesOfNumber() {
        assertThat(this.calculator.calcMultiply(numFilepath), `is`(24))
    }

    @Test
    fun concatenateOfString() {
        assertThat(this.calculator.concatenate(numFilepath), `is`("1234"))
    }
}