package com.indosatppi.kcli.process

import kotlinx.datetime.*
import kotliquery.Session
import kotliquery.queryOf
import org.apache.commons.csv.CSVFormat
import java.io.BufferedWriter
import java.sql.Connection
import org.apache.commons.csv.CSVPrinter
import java.io.File
import kotlin.time.Duration

class Process(private val db: Session) {
    fun printRef(path: String) {
        val workDate = Clock.System.now().plus(-1, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toLocalDateTime(
            TimeZone.currentSystemDefault())

        val qry = queryOf("""
            select distinct pvr, nvl(sid,'-') sid, nvl(origin,'-') origin,
                  product_name package_name, '' description,
                  0 price,  0 isrenewal
            from ppi_ngssp_daily_smy
            where period = ?
              and instr(statusdescription, 'SUCCESS')>0
              and requesttype in ('REGISTRATION','RENEWAL')
        """.trimIndent(), workDate.date.toString().replace("-", "")
        ).map(toNgsspDaily).asList

        val data: List<NgsspDaily> = db.run(qry)

        val writer = BufferedWriter(File(path).writer())
        val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT
            .builder().setHeader("PVR", "SID", "ORIGIN", "PACKAGE_NAME", "DESCRIPTION", "PRICE", "ISRENEWAL").build())

        for (res in data) {
            csvPrinter.printRecord(
                res.Pvr,
                res.SID,
                res.Origin,
                res.PackageName,
                res.Description,
                res.Price,
                res.IsRenewal
            )
        }

        csvPrinter.flush()
        csvPrinter.close()
    }
}