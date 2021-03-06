package utils

import methods.utils.Stats
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

object OvitoPrinter {


    fun printPositions(stats: List<Stats>, name: String, shouldPrint: Boolean = true) {
        if (!shouldPrint) return
        File("ovito").mkdirs()
        File("ovito/${name}").mkdirs()
        try {
            val planets = stats[0].positions.size
            for(statNum in 0 until stats.size){
                val theFile = File("ovito/${name}/positions_${statNum}")
                BufferedWriter(OutputStreamWriter(
                        FileOutputStream(theFile), "utf-8")).use { writer ->

                    writer.write("${planets}\n\n")
                    var planetNum = 0
                    stats[statNum].positions.forEach { (particleId, position) ->
                        val color = Planets.values().firstOrNull { it.ordinal == particleId }?.color ?: Color.default
                        val radius = Planets.values().firstOrNull { it.ordinal == particleId }?.radius ?: 1000.0
                        val radiusMultiplier = Planets.values().firstOrNull { it.ordinal == particleId }?.radiusMultiplier ?: 10000.0

                        writer.write("${planetNum++} ${position.x} ${position.y} ${color.red} ${color.green} ${color.blue} ${radius*radiusMultiplier}\n")
                    }
                    writer.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}