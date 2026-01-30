package toby.spring.user.dao

import java.sql.Connection

interface ConnectionMaker {
    fun makeConnection(): Connection
}