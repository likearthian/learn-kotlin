package com.indosatppi.kcli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.indosatppi.kcli.config.Config
import com.indosatppi.kcli.config.UseConfig
import com.indosatppi.kcli.config.initConfig
import com.indosatppi.kcli.logger.logger
import java.util.*

val USER_HOME = System.getProperties()["user.home"].toString()
const val DEFAULT_CONFIG_PATH = "/etc/kcli/config.yaml"

class Root: CliktCommand(name = "kcli") {
    val config by option("-c", "--config", help = "path to config file").default(DEFAULT_CONFIG_PATH, "default is $DEFAULT_CONFIG_PATH")
    val sets: List<String> by option("-s", "--set").multiple()
    init {
        println("init root")
//        val props = parseOverrideSets(sets)
//        initConfig(config, props)
    }
    override fun run() {
        val cfg: Config
        val props = parseOverrideSets(sets)
        try {
            initConfig(config, props)
            cfg = UseConfig()
        } catch(ex: Exception) {
            logger.error { ex.message }
            return
        }

        println("config path: $config")
        println("config: $cfg")
        println("props: $props")
    }
}

fun initRoot(): Root {
    return Root().subcommands(Run(), Conf())
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