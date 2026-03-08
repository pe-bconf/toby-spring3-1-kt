package toby.spring.user.domain

data class User (
    var id: String?, var name: String?, var password: String?,
    var level: Level?, var login: Int?, var recommend: Int?,
    var email: String?) {

    constructor(): this(null, null, null, null, null, null, null) {}
    fun upgradeLevel() {
        val nextLevel =
            (this.level
                ?: throw IllegalStateException("레벨이 설정되지 않은 사용자입니다.")).nextLevel()
                ?: throw IllegalStateException("${this.level}은 업그레이드가 불가능합니다.")

        this.level = nextLevel
    }
}