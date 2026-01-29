package toby.spring.connect

import java.sql.Connection

interface ConnectionMaker {
    fun makeConnection(): Connection
}