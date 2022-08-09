package com.indosatppi.kcli.worker

class Result<T>(private val value: T?, private val err: Error?) {
    fun isOk(): Boolean {
        return value != null
    }

    fun isErr(): Boolean {
        return !this.isOk()
    }

    fun Map(mapFn: (v: T) -> Unit): Result<T> {
        if (this.value != null) {
            mapFn(this.value)
        }
        return this
    }

    fun MapErr(mapFn: (err: Error) -> Unit): Result<T> {
        if (this.err != null) {
            mapFn(this.err)
        }

        return this
    }
}

fun <T>Ok(value: T): Result<T> {
    return Result(value, null)
}

fun <T>Err(err: Error): Result<T> {
    return Result(null, err)
}
