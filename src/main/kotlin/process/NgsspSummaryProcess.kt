package com.indosatppi.kcli.process

import com.indosatppi.kcli.config.Config
import com.indosatppi.kcli.utils.SqlIN
import kotlinx.datetime.*
import kotliquery.Session
import kotliquery.queryOf

class NgsspSummaryProcess(private val oraDb: Session, cfg: Config) {
    fun oraDeleteNgsspSummary() {
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

        val strSql = "DELETE FROM ppi_admin.ppi_ngssp_daily_smy WHERE period in (?)"
        val (newQry, args) = SqlIN(strSql, dateList)
        println("query:\n${newQry}")
        println("args: \n${args.joinToString("\n")}")
    }

    fun OraTestQuery() {
        val periods = listOf<String>("20200101", "20200102", "20210101", "20210102")
        var strSql = """
            SELECT 
                period
                ,count(*) jumlah
            FROM 
                ppi_admin.ppi_ngssp_daily_smy 
            WHERE period IN (?) GROUP BY period"""

        val (newQry, args) = SqlIN(strSql, periods)
        val qry = queryOf(newQry, *args.toTypedArray())
            .map{
                "${it.string(1)}-${it.int(2)}"
            }.asList

        val res = oraDb.run(qry)
        println("result: $res")
    }

    fun HdpDel() {
        
    }
}

private fun formatDate(sdate: LocalDateTime): String {
    return "${sdate.year}${sdate.monthNumber.toString().padStart(2, '0')}${sdate.dayOfMonth.toString().padStart(2, '0')}"
}