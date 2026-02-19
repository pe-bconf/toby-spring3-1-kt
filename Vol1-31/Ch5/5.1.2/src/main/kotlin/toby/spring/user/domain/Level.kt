package toby.spring.user.domain

enum class Level {
    BASIC(1), SILVER(2), GOLD(3);

    private var value: Int;

    constructor(value: Int) {
        this.value = value
    }

    fun intValue(): Int {
        return this.value
    }

    companion object {
        fun valueOf(value: Int): Level {
            return when (value) {
                1 -> BASIC
                2 -> SILVER
                3 -> GOLD
                else -> throw AssertionError("Unknown value : $value")
            }
        }
    }
}