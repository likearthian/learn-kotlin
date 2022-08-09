package com.indosatppi.kcli.worker

import java.io.InputStream

interface ParseSetter {
    fun ParseCsvLine(keys: List<String>, data: List<String>)
//    fun ParseJson(data: ByteArray)
}

enum class PayloadType {
    PL_CSV,
    PL_JSON,
}

class Payload(private val PType: PayloadType, private val Keys: List<String>, private val Data: ByteArray) {
    fun <T>RowsToListOf(factory: () -> T): List<T> where T: ParseSetter {
        val list = mutableListOf<T>()
        val res = this.readAllCsv(this.Data.inputStream())
        for (v in res) {
            val o = factory()
            val values = mutableListOf<String>()
            Keys.forEach {
                values.add(v[it]!!)
            }

            o.ParseCsvLine(Keys, values)
            list.add(o)
        }

        return list
    }

    fun RowsToListOfMap(): List<Map<String, Any>> {
        return when (this.PType) {
            PayloadType.PL_CSV -> this.readAllCsv(this.Data.inputStream())
            else -> this.readAllCsv(this.Data.inputStream())
        }
    }

    private fun readAllCsv(inputStream: InputStream): List<Map<String, String>> {
        val result: MutableList<Map<String, String>> = mutableListOf<Map<String, String>>()
        val reader = inputStream.bufferedReader()
        reader.lineSequence()
            .filter { it.isNotBlank() }
            .map{
                val ldata = it.split(',', limit = this.Keys.count())
                val dmap = HashMap<String, String>()
                for (i in this.Keys.indices) {
                    dmap[this.Keys[i]] = ldata[i]
                }

                result.add(dmap)
            }

        return result
    }


}