package toby.service.connect

import java.sql.Connection

interface ConnectionMaker {
    fun makeConnection(): Connection
}