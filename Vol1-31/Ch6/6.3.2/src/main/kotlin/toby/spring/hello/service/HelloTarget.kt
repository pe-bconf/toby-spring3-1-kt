package toby.spring.hello.service

class HelloTarget: Hello {
    override fun sayHello(name: String): String {
        return "Hello $name"
    }

    override fun sayHi(name: String): String {
        return "Hi $name"
    }

    override fun sayThankYou(name: String): String {
        return "Thank You $name"
    }
}