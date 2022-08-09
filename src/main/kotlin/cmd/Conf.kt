package com.indosatppi.kcli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.indosatppi.kcli.config.*
import com.indosatppi.kcli.logger.logger
import java.util.*

class Conf: CliktCommand(name = "conf", help = "test config reading") {
    val config by option("-c", "--config", help = "path to config file").default(DEFAULT_CONFIG_PATH, "default is $DEFAULT_CONFIG_PATH")
    val sets: List<String> by option("-s", "--set").multiple()
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