package org.example.org.example

import java.io.FileWriter
import java.io.PrintWriter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SqlGenerator {
    val filename: String

    constructor(filename: String) {
        this.filename = filename
        this.writer = PrintWriter(FileWriter(filename))
    }

    val writer: PrintWriter
    fun generateSqls( nmi:String,intervalDate: String, consumptions: List<String>) : List<String> {
        val list = mutableListOf<String>()
        var datetime = LocalDate.parse(intervalDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay()
        consumptions.forEach {
            val str = generateSql(nmi, datetime, BigDecimal(it))
            list.add(str)
            datetime = datetime.plusMinutes(30)
        }
        return list
    }

    fun generateSql(nmi:String, timestamp: LocalDateTime, consumption: BigDecimal) : String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val template = """
    insert into meter_readings (nmi, timestamp, consumption) values('${nmi}', '${timestamp.format(formatter)}', ${consumption});
""".trimIndent()
        //println(template)
        writer.println(template)
        return template
    }

    fun close() {
        writer.close()
    }
}


