package tools.math

import alexey.tools.common.math.ImmutableIntVector2
import com.badlogic.gdx.math.Vector2

fun getWorldPosition(intVector2: ImmutableIntVector2, chunkSize: Float): Vector2 {

    fun fixCoordinate(coord: Int): Float {
        return if (coord < 0) (coord + 1) * chunkSize - chunkSize else coord * chunkSize
    }

    return Vector2(fixCoordinate(intVector2.x), fixCoordinate(intVector2.y))
}
