package com.indosatppi.kcli.config

import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.parsers.PropsPropertySource
import java.util.*
import kotlin.io.path.Path

data class Database(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val dbname: String,
)

data class Hadoop(
    val hdfsPrincipalName: String,
    val keytabFile: String,
)

data class Config(
    val database: Database,
    val listenPort: Int,
    val datediff: Int,
    val hadoop: Hadoop,
)

private var ConfigInstance: Config? = null

fun UseConfig(): Config {
    if (ConfigInstance != null) {
        return ConfigInstance as Config
    }
    throw Exception("config uninitialized")
}
fun initConfig(configFilePath: String, overrideProps: Properties) {
    ConfigInstance = ConfigLoaderBuilder.default()
        .addPropertySource(PropsPropertySource(overrideProps))
        .addEnvironmentSource(true, true)
        .addSource(PropertySource.path(Path(configFilePath), true))
        .addResourceSource("/default.yaml")
        .build()
        .loadConfigOrThrow<Config>()
}
