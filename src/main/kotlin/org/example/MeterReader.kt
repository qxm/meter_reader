package org.example

import org.example.RecordConstants.FIELD_INTERVAL_VALUE_START
import org.example.org.example.SqlGenerator
import java.io.File

class MeterReader(val input: String, val sqlGenerator: SqlGenerator) {
    fun read() {
        var nmi = ""
        val field_interval_length = 8
        var size : Int = 0;
        File(input).forEachLine {
            val items = it.split(",");
            when (getRecordType(items[RecordConstants.FIELD_TYPE])) {
                org.example.Record.DataDetails -> {
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