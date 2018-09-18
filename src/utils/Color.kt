package utils

class Color(red: Double, green: Double, blue: Double) {
    val red: Double
    val green: Double
    val blue: Double

    init {
        val sum = red + green + blue
        this.red = red/255
        this.green = green/255
        this.blue = blue/255
    }

    companion object {
        val default = Color(100.0,100.0,100.0)
    }
}