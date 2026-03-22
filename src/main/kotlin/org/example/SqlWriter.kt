package org.example

import kotlinx.coroutines.channels.Channel
import java.io.File

class SqlWriter {
    val filename: String

    constructor(filename: String, channel: Channel<String>) {
        this.filename = filename
        this.dstFile = File(filename)
        this.channel = channel
    }

    val dstFile: File
    val channel: Channel<String>

    suspend fun writeFile() {
        dstFile.bufferedWriter().use { out ->
            for (str in channel) {
                //println(str)
                out.append(str)
                out.append("\n")
            }
        }
    }
}