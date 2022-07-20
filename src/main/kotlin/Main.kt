package com.indosatppi.kcli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.indosatppi.kcli.config.Config

import com.indosatppi.kcli.config.initConfig

import com.indosatppi.kcli.logger.logger

import com.indosatppi.kcli.utils.createOraDbConn
import com.indosatppi.kcli.process.Process
import kotliquery.Session
import java.sql.SQLException
import java.util.Properties


val USER_HOME = System.getProperties()["user.home"].toString()
const val DEFAULT_CONFIG_PATH = "/etc/kcli/config.yaml"

class Root(): CliktCommand(name = "kcli") {
    override fun run() = Unit
}

class RunConfig(): CliktCommand(name = "conf", help = "test config reading") {
    val config by option("-c", "--config", help = "path to config file").default(DEFAULT_CONFIG_PATH, "default is $DEFAULT_CONFIG_PATH")
    val sets: List<String> by option("-s", "--set").multiple()
    override fun run() {
        val cfg: Config
        val props = parseOverrideSets(sets)
        try {
            cfg = initConfig(config, props)
        } catch(ex: Exception) {
            logger.error { ex.message }
            return
        }

        println("config path: $config")
        println("config: $cfg")
        println("props: $props")
//        TODO("Not yet implemented")
    }

    fun parseOverrideSets(list: List<String>): Properties {
        val props = Properties()
        for (str in list) {
            val slice = str.split("=")
            val k = slice[0].trim()
            var v = ""
            if (slice.count() > 1) v = slice[1].trim()
            props.setProperty(k, v)
        }

        return props
    }
}

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
fun main(args: Array<String>) {
    val cmd = Root().subcommands(Run(), RunConfig())
    cmd.main(args)
}