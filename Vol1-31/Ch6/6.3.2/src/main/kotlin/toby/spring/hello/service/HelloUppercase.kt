package toby.spring.hello.service

class HelloUppercase: Hello {
    private val hello: Hello

    constructor(hello: Hello) {
        this.hello = hello
    }

    override fun sayHello(name: String): String {
        return hello.sayHello(name).uppercase()
    }

    override fun sayHi(name: String): String {
        return hello.sayHi(name).uppercase()
    }

    override fun sayThankYou(name: String): String {
        return hello.sayThankYou(name).uppercase()
    }
}