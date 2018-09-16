package utils

import java.io.BufferedReader
import java.io.File

class CSVReader {
    companion object {
        fun doForEachDay(process: (DayData) -> Unit) {
            val earth = File("planets/earth.csv").bufferedReader().lineSequence()
            val jupiter = File("planets/jupiter.csv").bufferedReader().lineSequence()
            val saturn = File("planets/saturn.csv").bufferedReader().lineSequence()

            earth.zip(saturn).zip(jupiter).forEach {
                val earthData = it.first.first.split(",")
                val jupiterData = it.first.second.split(",")
                val saturnData = it.second.split(",")

                val particles = ArrayList<Particle>()

                particles.add(Planets.EARTH.generate(
                        earthData[2].toDouble(),
                        earthData[3].toDouble(),
                        earthData[5].toDouble(),
                        earthData[6].toDouble())
                )

                particles.add(Planets.JUPITER.generate(
                        jupiterData[2].toDouble(),
                        jupiterData[3].toDouble(),
                        jupiterData[5].toDouble(),
                        jupiterData[6].toDouble())
                )

                particles.add(Planets.SATURN.generate(
                        saturnData[2].toDouble(),
                        saturnData[3].toDouble(),
                        saturnData[5].toDouble(),
                        saturnData[6].toDouble())
                )

                particles.add(Planets.SUN.generateBasic())

                process(DayData(earthData[1], particles))
            }
        }
    }
}