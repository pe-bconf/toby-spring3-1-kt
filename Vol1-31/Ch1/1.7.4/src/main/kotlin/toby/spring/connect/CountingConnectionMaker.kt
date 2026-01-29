package toby.spring.connect

import toby.spring.config.JDBC_URL
import java.sql.Connection
import java.sql.DriverManager

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