package toby.spring.dao

import java.sql.Connection

interface ConnectionMaker {
    fun makeConnection(): Connection
}