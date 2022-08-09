package com.indosatppi.kcli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

import com.indosatppi.kcli.cmd.Run
import com.indosatppi.kcli.cmd.Conf
import com.indosatppi.kcli.cmd.Root

val USER_HOME = System.getProperties()["user.home"].toString()
const val DEFAULT_CONFIG_PATH = "/etc/kcli/config.yaml"

fun main(args: Array<String>) {
    val cmd = Root().subcommands(Run(), Conf())
    cmd.main(args)
}