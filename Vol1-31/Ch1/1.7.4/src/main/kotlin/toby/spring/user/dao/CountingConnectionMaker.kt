package toby.spring.user.dao

import java.sql.Connection

class CountingConnectionMaker: ConnectionMaker {
    var counter = 0
    lateinit var connectionMaker: ConnectionMaker

    constructor(realConnectionMaker: ConnectionMaker): super() {
        this.connectionMaker = realConnectionMaker
    }

    override fun makeConnection(): Connection {
        this.counter++
        return this.connectionMaker.makeConnection()
    }
}