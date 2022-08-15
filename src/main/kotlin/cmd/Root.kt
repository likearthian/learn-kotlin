package com.indosatppi.kcli.cmd

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.indosatppi.kcli.config.Config
import com.indosatppi.kcli.config.initConfig
import com.indosatppi.kcli.logger.logger
import java.util.*

val USER_HOME = System.getProperties()["user.home"].toString()
const val DEFAULT_CONFIG_PATH = "/etc/kcli/config.yaml"

class CommonOptions: OptionGroup("Common Options") {
    val config by option("-c", "--config", help = "path to config file").default(DEFAULT_CONFIG_PATH, "default is $DEFAULT_CONFIG_PATH")
    val sets: List<String> by option("-s", "--set").multiple()
}

abstract class BaseCommand(
    help: String = "",
    epilog: String = "",
    name: String? = null,
    invokeWithoutSubcommand: Boolean = false,
    printHelpOnEmptyArgs: Boolean = false,
    helpTags: Map<String, String> = emptyMap(),
    autoCompleteEnvvar: String? = "",
    allowMultipleSubcommands: Boolean = false,
    treatUnknownOptionsAsArgs: Boolean = false,
    hidden: Boolean = false
) : CliktCommand(
    help,
    epilog,
    name,
    invokeWithoutSubcommand,
    printHelpOnEmptyArgs,
    helpTags,
    autoCompleteEnvvar,
    allowMultipleSubcommands
) {
    private val config by option("-c", "--config", help = "path to config file").default(DEFAULT_CONFIG_PATH, "default is $DEFAULT_CONFIG_PATH")
    private val sets: List<String> by option("-s", "--set").multiple()

    override fun run() {
        val config = config
        val props = parseOverrideSets(sets)
        val cfg = initConfig(config, props)
        try {
            currentContext.findOrSetObject { cfg }
        } catch(ex: Exception) {
            logger.error { ex.message }
            return
        }

        println("config path: $config")
        println("props: $props")
    }
}

class Root: NoOpCliktCommand()

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