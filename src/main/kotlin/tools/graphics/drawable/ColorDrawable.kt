package tools.graphics.drawable

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable


class ColorDrawable(
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float
) : BaseDrawable() {

    private val savedBatchColor = Color()

    override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
        savedBatchColor.set(batch.color)
        batch.setColor(r, g, b, a)
        batch.draw(Assets.blankWhite, x, y, width, height);
        batch.color = savedBatchColor
    }

}
