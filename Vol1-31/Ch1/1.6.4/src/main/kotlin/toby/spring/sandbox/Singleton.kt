package toby.spring.sandbox

class Singleton {
    private constructor() {}

    companion object {
        private var instance: Singleton? = null
        @JvmStatic
        @Synchronized
        fun getInstance(): Singleton {
            if (instance == null) {
                instance = Singleton()
            }
            return instance!!
        }
    }

}