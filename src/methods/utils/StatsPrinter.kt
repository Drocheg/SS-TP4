package methods.utils

import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

object StatsPrinter {

    var outputDirectory = "stats_default"
        set(value) {
            File("stats").mkdirs()
            File("stats/$value").mkdirs()
            field = "stats/$value"
        }


    fun printPositions(stats: List<Stats>) {
        val dir = outputDirectory
        try {
            val theFile = File("$dir/positions.dat")
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->
                for(stat in stats) {
                    writer.write("${stat.time} ${stat.energy}")
                    stat.positions.forEach { (_, position) ->
                        writer.write(" ${position.x} ${position.y}")
                    }
                    writer.write("\n")
                }
                writer.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}