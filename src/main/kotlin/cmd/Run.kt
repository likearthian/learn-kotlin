package com.indosatppi.kcli.cmd

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.indosatppi.kcli.config.Config
import com.indosatppi.kcli.utils.createOraDbConn
import kotliquery.Session
import java.sql.SQLException
import com.indosatppi.kcli.logger.logger
import com.indosatppi.kcli.process.NgsspSummaryProcess
import com.indosatppi.kcli.process.Process

class Run(): BaseCommand(help = "run NgsspDailySummary") {
    private val ktab by option("-k", "--ktab", help = "path to keytab file").default(USER_HOME, "default is $USER_HOME")
    private val cfg by requireObject<Config>()
    init {
        println("run init")
    }
    override fun run() {
        super.run()
        logger.info{ "keytab file is $ktab"}
        println("config: $cfg")
        val db: Session
        try {
            db = createOraDbConn(cfg.database.host, cfg.database.port, cfg.database.dbname, cfg.database.user, cfg.database.password)
        } catch (ex: SQLException){
            logger.error { ex.message }
            return
        }

        runNgsspSummary(db, cfg)

        logger.info { "process done" }
    }

    fun runPrintRef(db: Session) {
        val pr = Process(db)

        try {
            pr.printRef("listSample.csv")
        } catch (ex: SQLException) {
            logger.error { ex.message }
            return
        }
    }

    fun runNgsspSummary(db: Session, cfg: Config) {
        val ng = NgsspSummaryProcess(db, cfg)
        ng.oraDeleteNgsspSummary()
    }
}