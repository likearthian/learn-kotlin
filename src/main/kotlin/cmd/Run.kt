package com.indosatppi.kcli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.indosatppi.kcli.utils.createOraDbConn
import kotliquery.Session
import java.sql.SQLException
import com.indosatppi.kcli.logger.logger
import com.indosatppi.kcli.process.Process

class Run(): CliktCommand(help = "Run job") {
    val ktab by option("-k", "--ktab", help = "path to keytab file").default(USER_HOME, "default is $USER_HOME")
    override fun run() {

        logger.info{ "keytab file is $ktab"}
        val db: Session
        try {
            db = createOraDbConn("10.34.144.221", 1525, "opmfs", "ppi_admin", "widirianto")
        } catch (ex: SQLException){
            logger.error { ex.message }
            return
        }

        val pr = Process(db)

        try {
            pr.printRef("listSample.csv")
        } catch (ex: SQLException) {
            logger.error { ex.message }
            return
        }

        logger.info { "process done" }
    }
}