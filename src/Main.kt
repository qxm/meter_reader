import RecordConstants.FIELD_INTERVAL_VALUE_START
import RecordConstants.FIELD_TYPE
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.String

enum class Record{
    Header,
    DataDetails,
    IntervalData,
    IntervalEvent,
    B2BDetails,
    End,
    ERROR
}

object RecordConstants {
    val HEADER_TYPE = "100"
    val DATA_DETAILS_TYPE = "200"
    val INTERVAL_DATA_TYPE = "300"
    val INTERVAL_EVENT_TYPE = "400"
    val B2B_DETAILS_TYPE = "500"
    val END_TYPE = "500"

    val INTERVAL_DIVIDEND = 1440
    val FIELD_TYPE = 0
    val FIELD_NMI = 1
    val FIELD_INTERVAL_VALUE_START = 2
    val FILED_INTERVAL_DATE = 1
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


class MeterReader(val input: String, val sqlGenerator: SqlGenerator) {
    fun read() {
        var nmi = ""
        val field_interval_length = 8
        var size : Int = 0;
        File(input).forEachLine {
            val items = it.split(",");
            when (getRecordType(items[FIELD_TYPE])) {
                Record.DataDetails -> {
                    nmi = items[RecordConstants.FIELD_NMI];
                    size = RecordConstants.INTERVAL_DIVIDEND/items[field_interval_length].toInt();
                }
                Record.IntervalData -> {
                    val intervalDate = items[RecordConstants.FILED_INTERVAL_DATE];
                    sqlGenerator.generateSqls(
                        nmi,
                        intervalDate,
                        items.slice(FIELD_INTERVAL_VALUE_START..FIELD_INTERVAL_VALUE_START + size - 1)
                    )
                }

                else -> {}
            }
        }
        sqlGenerator.close()
    }

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



