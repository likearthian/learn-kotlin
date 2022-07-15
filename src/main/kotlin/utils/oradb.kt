package com.indosatppi.kcli.utils

import java.sql.Connection
import java.sql.Statement
import java.sql.DriverManager
import kotliquery.Session
import kotliquery.sessionOf

fun createOraDbConn(host: String, port: Int, sid: String, user: String, pass: String): Session {
    val jdbcUrl = "jdbc:oracle:thin:@${host}:${port}:${sid}"
//    val conn = DriverManager.getConnection(jdbcUrl, user, pass)
    return sessionOf(jdbcUrl, user, pass)
}