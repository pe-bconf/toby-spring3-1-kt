package toby.spring.user.domain

data class User(
    var id: String?, var name: String?, var password: String?,
    var level: Level?, var login: Int?, var recommend: Int?) {
    constructor(): this(null, null, null, null, null, null) {}
}