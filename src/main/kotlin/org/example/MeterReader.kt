package org.example

import kotlinx.coroutines.channels.Channel
import org.example.RecordConstants.FIELD_INTERVAL_VALUE_START
import java.io.File

class MeterReader(val input: String, val channel: Channel<String>) {
    suspend fun read() {
        var nmi = ""
        println("Reading $input")
        val field_interval_length = 8
        var size: Int = 0
        val sqlGenerator = SqlGenerator(channel)
        File(input).bufferedReader().use {
            var line = it.readLine()
            while (line != null) {
                val items = line.split(",")
                when (getRecordType(items[RecordConstants.FIELD_TYPE])) {
                    Record.DataDetails -> {
                        nmi = items[RecordConstants.FIELD_NMI]
                        size = RecordConstants.INTERVAL_DIVIDEND / items[field_interval_length].toInt()
                    }

                    Record.IntervalData -> {
                        val intervalDate = items[RecordConstants.FILED_INTERVAL_DATE]
                        val consumptions =
                            items.slice(FIELD_INTERVAL_VALUE_START..FIELD_INTERVAL_VALUE_START + size - 1)

                        sqlGenerator.generateSqls(nmi, intervalDate, consumptions)

                    }

                    else -> {}
                }
                line = it.readLine()
            }
        }
    }

}