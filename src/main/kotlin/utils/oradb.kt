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

fun SqlIN(qry: String, vararg params: Any): Pair<String, List<Any>> {
    val phCount = qry.length - qry.replace("?", "").length
    if (phCount != params.size) {
        throw Exception("number of param is not the same as the placeholder")
    }

    var sx = 0
    var newQry = ""
    val newArgs = mutableListOf<Any>()
    for (arg in params) {
        val ph: List<String>
        val isArray = when (arg) {
            is Array<*> -> {
                ph = arg.map{ "?" }
                arg.forEach{
                    newArgs.add(it!!)
                }
                true
            }
            is IntArray -> {
                ph = arg.map { "?" }
                arg.forEach{
                    newArgs.add(it)
                }
                true
            }
            is List<*> -> {
                ph = arg.map { "?" }
                arg.forEach{
                    newArgs.add(it!!)
                }
                true
            }
            else -> {
                ph = listOf("?")
                newArgs.add(arg)
                false
            }
        }

        val idx = qry.indexOf("?", sx)
        if (idx > -1) {
            newQry += qry.substring(sx until idx) + ph.joinToString(",")
            sx = idx + 1
        }
    }

    if (sx + 1 <= qry.length) {
        newQry += qry.substring(sx)
    }

    return newQry to newArgs
}