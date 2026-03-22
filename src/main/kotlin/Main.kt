package org.example
import org.example.org.example.SqlGenerator

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val sqlGenerator = SqlGenerator("sql.txt");
    val reader = MeterReader("example.txt", sqlGenerator)
    reader.read()

}






