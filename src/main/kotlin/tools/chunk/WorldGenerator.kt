package tools.chunk

import alexey.tools.common.level.Chunk
import com.badlogic.gdx.math.Vector2
import tools.math.getWorldPosition

abstract class WorldGenerator(private val chunkSize: Float,
                              private val blockSize: Float) {

    protected abstract fun onGenerateChunk(chunk: Chunk, positions: Array<Vector2>)

    fun generateChunk(chunk: Chunk) {
        val chunkPosition = getWorldPosition(chunk.getPosition(), chunkSize)
        val cells = (chunkSize / blockSize).toInt()

        val positions = Array(cells * cells) { Vector2() }
        var idx = 0
        for (y in 0 until cells) {
            for (x in 0 until cells) {
                positions[idx++].set(
                    x * blockSize + chunkPosition.x,
                    y * blockSize + chunkPosition.y
                )
            }
        }

        onGenerateChunk(chunk, positions)
    }
}