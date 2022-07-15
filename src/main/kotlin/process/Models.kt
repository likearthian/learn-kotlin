package com.indosatppi.kcli.process

import kotliquery.Row
import java.sql.Timestamp

data class PvrPriceRef(
    var Pvr: String,
    var Origin: String,
    var Price: Float,
    var CreatedBy: String,
    var CreatedAt: String,
    var UpdatedBy: String,
    var UpdatedAt: Timestamp
)

data class NgsspDaily(
    var Pvr: String,
    var SID: String,
    var Origin: String,
    var PackageName: String?,
    var Description: String?,
    var Price: Float,
    var IsRenewal: Int,
)

val toNgsspDaily: (Row) -> NgsspDaily = {
    NgsspDaily(
        it.string("pvr"),
        it.string("sid"),
        it.string("origin"),
        it.stringOrNull("package_name"),
        it.stringOrNull("description"),
        it.float("price"),
        it.int("isrenewal"),
    )
}

