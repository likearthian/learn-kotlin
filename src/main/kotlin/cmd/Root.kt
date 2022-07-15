package com.indosatppi.kcli.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import java.util.*

val USER_HOME = System.getProperties()["user.home"].toString()
const val DEFAULT_CONFIG_PATH = "/etc/kcli/config.yaml"

class Root: CliktCommand(name = "kcli") {
    override fun run() = Unit
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