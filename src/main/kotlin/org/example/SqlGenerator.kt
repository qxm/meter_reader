package org.example

import kotlinx.coroutines.channels.Channel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SqlGenerator(val channel: Channel<String>) {

    suspend fun generateSqls( nmi:String,intervalDate: String, consumptions: List<String>) : List<String> {
        val list = mutableListOf<String>()
        var datetime = LocalDate.parse(intervalDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay()
        consumptions.forEach {
            val str = generateSql(nmi, datetime, BigDecimal(it))
            list.add(str)
            datetime = datetime.plusMinutes(30)
        }
        return list
    }

    suspend fun generateSql(nmi:String, timestamp: LocalDateTime, consumption: BigDecimal) : String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val template = """
    insert into meter_readings (nmi, timestamp, consumption) values('${nmi}', '${timestamp.format(formatter)}', ${consumption});
""".trimIndent()
        //println(template)
        channel.send(template)
        return template
    }


}


