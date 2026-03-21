package toby.spring.hello.service

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.Locale
import java.util.Locale.getDefault

class UppercaseHandler: InvocationHandler {
    private val target: Any

    constructor(target: Any) {
        this.target = target
    }

    override fun invoke(
        proxy: Any?,
        method: Method?,
        args: Array<out Any?>?
    ): Any? {
        // 타깃 위임 함수 실행
        val ret = method?.invoke(target, *args ?: emptyArray())

        // 문자열이 아니거나 함수명이 say로 시작하지 않는다면
        if (ret !is String || !method.name.startsWith("say")) {
            return ret;
        }

        // 프록시의 부가 기능 실행
        return ret.uppercase(getDefault())
    }
}