package org.example


import org.example.org.example.RecordConstants
import org.example.org.example.RecordConstants.FIELD_INTERVAL_VALUE_START
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class Record{
    Header,
    DataDetails,
    IntervalData,
    IntervalEvent,
    B2BDetails,
    End,
    ERROR
}


fun getRecordType(type: String) =
    when (type) {
        RecordConstants.HEADER_TYPE ->   Record.Header;
        RecordConstants.DATA_DETAILS_TYPE ->   Record.DataDetails;
        RecordConstants.INTERVAL_DATA_TYPE ->   Record.IntervalData;
        RecordConstants.INTERVAL_EVENT_TYPE ->   Record.IntervalEvent;
        RecordConstants.B2B_DETAILS_TYPE ->   Record. B2BDetails;
        RecordConstants.END_TYPE ->   Record.End;
        else -> Record.ERROR;
    }


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val sqlGenerator = SqlGenerator("sql.txt");
    val reader = MeterReader("example.txt", sqlGenerator)
    reader.read()

}





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



