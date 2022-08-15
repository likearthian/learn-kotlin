package com.indosatppi.kcli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.*
import com.indosatppi.kcli.config.*
import com.indosatppi.kcli.logger.logger
import java.util.*

class Conf: CliktCommand(name = "conf", help = "test config reading") {
    val config by requireObject<Config>()
    override fun run() {
        println("config path: $config")
//        TODO("Not yet implemented")
    }

}