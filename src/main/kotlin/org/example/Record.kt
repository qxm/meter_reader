package org.example

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
