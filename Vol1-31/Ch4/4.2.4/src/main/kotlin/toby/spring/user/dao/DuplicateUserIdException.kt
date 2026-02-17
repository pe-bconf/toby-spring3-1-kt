package toby.spring.user.dao

class DuplicateUserIdException: Exception {
    constructor():super() {}
    constructor(e: Throwable):super(e)
}