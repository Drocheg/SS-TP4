package utils

// Basic
enum class Planets(val radius: Double,
                   val mass: Double,
                   val baseX: Double,
                   val baseY: Double,
                   val baseVx: Double,
                   val baseVy: Double) {

    EARTH(6378.137,
            5.97219e24,
            1.439499338063904E+08,
            -4.501039010586976E+07,
            8.415311788888911E+00,
            2.831597743867901E+01
            ),

    SUN(695700.0,
            1988500e24,
            0.0,0.0,0.0,0.0),

    JUPITER(69911.0,
            1898.13e24,
            1.058409319749973E+08,
            7.551533616543298E+08,
            -1.310553498751214E+01,
            2.424702557330317E+00),

    SATURN(58232.0,
            5.6834e26,
            -1.075592980165280E+09,
            8.544801191238763E+08,
            -6.541480412587013E+00,
            -7.590568168145934E+00);

    fun generateBasic(): Particle = generate(baseX, baseY, baseVx, baseVy)

    fun generate(x: Double, y: Double, vx: Double, vy: Double) = Particle(this.ordinal, Vector(x,y), Vector(vx, vy), radius, mass)

}