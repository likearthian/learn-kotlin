package com.indosatppi.kcli.process

import com.indosatppi.kcli.config.UseConfig
import kotlinx.datetime.*
import kotliquery.Session
import org.apache.hadoop.fs.FileSystem

class NgsspSummaryProcess(private val oraDb: Session) {
    fun OraDeleteNgsspSummary() {
        val cfg = UseConfig()
        val now = Clock.System.now()
        val endDate = Clock.System.now().plus(-1, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toLocalDateTime(
            TimeZone.currentSystemDefault())

        val startDate = Clock.System.now().plus(-31, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toLocalDateTime(
            TimeZone.currentSystemDefault())

        val dateList = mutableListOf<String>()
        for (i in 1..31) {
            val sdate = formatDate(now.minus(i, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toLocalDateTime(
                TimeZone.currentSystemDefault()))

            dateList.add(sdate)
        }

        val strSql = "DELETE FROM ppi_admin.ppi_ngssp_daily_smy WHERE period in ('${dateList.joinToString("',\n'")}')"
        println("query:\n${strSql}")
    }
}

private fun formatDate(sdate: LocalDateTime): String {
    return "${sdate.year}${sdate.monthNumber.toString().padStart(2, '0')}${sdate.dayOfMonth.toString().padStart(2, '0')}"
}