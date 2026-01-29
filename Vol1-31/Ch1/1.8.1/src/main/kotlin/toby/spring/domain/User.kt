package toby.spring.domain

data class User(var id: String, var name: String, var password: String) {
    constructor(): this("", "", "") {}
}