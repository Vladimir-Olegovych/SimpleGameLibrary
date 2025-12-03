package tools.noice.simplex

import kotlin.random.Random

class SimplexNoise(private val seed: Long) {
    private val perm = IntArray(512)
    private val permMod12 = IntArray(512)

    init {
        val random = Random(seed)
        val p = IntArray(256) { it }
        p.shuffle(random)

        for (i in 0 until 512) {
            perm[i] = p[i and 255]
            permMod12[i] = perm[i] % 12
        }
    }

    private fun fastFloor(x: Float): Int {
        val xi = x.toInt()
        return if (x < xi) xi - 1 else xi
    }

    private fun dot(g: Int, x: Float, y: Float): Float {
        return when (g % 12) {
            0 -> x + y
            1 -> -x + y
            2 -> x - y
            3 -> -x - y
            4 -> x
            5 -> -x
            6 -> y
            7 -> -y
            8 -> 0.5f * x + y
            9 -> -0.5f * x + y
            10 -> x - 0.5f * y
            11 -> -x - 0.5f * y
            else -> 0f
        }
    }

    fun noise2D(xin: Float, yin: Float): Float {
        // Skew the input space
        val s = (xin + yin) * F2
        val i = fastFloor(xin + s)
        val j = fastFloor(yin + s)
        val t = (i + j) * G2

        // Unskew the cell origin back to (x,y) space
        val X0 = i - t
        val Y0 = j - t

        // The x,y distances from the cell origin
        val x0 = xin - X0
        val y0 = yin - Y0

        // Determine which simplex we're in
        val (i1, j1) = if (x0 > y0) {
            Pair(1, 0) // Lower triangle, XY order
        } else {
            Pair(0, 1) // Upper triangle, YX order
        }

        // Offsets for middle corner
        val x1 = x0 - i1 + G2
        val y1 = y0 - j1 + G2

        // Offsets for last corner
        val x2 = x0 - 1f + 2f * G2
        val y2 = y0 - 1f + 2f * G2

        // Work out the hashed gradient indices
        val ii = i and 255
        val jj = j and 255

        val gi0 = permMod12[ii + perm[jj]]
        val gi1 = permMod12[ii + i1 + perm[jj + j1]]
        val gi2 = permMod12[ii + 1 + perm[jj + 1]]

        // Calculate the contribution from the three corners
        var t0 = 0.5f - x0 * x0 - y0 * y0
        val n0 = if (t0 < 0) 0f else {
            t0 *= t0
            t0 * t0 * dot(gi0, x0, y0)
        }

        var t1 = 0.5f - x1 * x1 - y1 * y1
        val n1 = if (t1 < 0) 0f else {
            t1 *= t1
            t1 * t1 * dot(gi1, x1, y1)
        }

        var t2 = 0.5f - x2 * x2 - y2 * y2
        val n2 = if (t2 < 0) 0f else {
            t2 *= t2
            t2 * t2 * dot(gi2, x2, y2)
        }

        // Add contributions and scale to return value
        return 70f * (n0 + n1 + n2)
    }

    fun noise2D(x: Float, y: Float, frequency: Float): Float {
        return noise2D(x * frequency, y * frequency)
    }

    companion object {
        const val F2 = 0.3660254f
        const val G2 = 0.21132487f
    }
}