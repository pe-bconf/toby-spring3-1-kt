package springbook.learntest.template

interface LineCallback<T> {
    fun doSomethingWithLine(line: String, value: T): T
}