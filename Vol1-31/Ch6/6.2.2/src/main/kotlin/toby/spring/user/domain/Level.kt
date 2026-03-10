package toby.spring.user.domain

enum class Level {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private var value: Int;
    private var next: Level? = null;

    constructor(value: Int, next: Level?) {
        this.value = value
        if (next != null) {
            this.next = next
        }
    }

    fun intValue(): Int {
        return this.value
    }

    fun nextLevel(): Level? {
        return next
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