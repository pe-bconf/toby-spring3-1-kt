package toby.spring.user.dao

class DuplicateUserIdException: RuntimeException {
    constructor():super() {}
    constructor(e: Throwable):super(e)
}