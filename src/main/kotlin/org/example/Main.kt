package org.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val channel = Channel<String>()//(capacity = Channel.UNLIMITED)
    val reader = MeterReader("example.txt", channel)

    runBlocking {
        val writeJob = launch {
            val sqlWriter = SqlWriter("sql.txt", channel)
            sqlWriter.writeFile()
        }
        val readJob = launch {
            reader.read()
        }
        readJob.join()
        channel.close()

        writeJob.join()
    }




}






