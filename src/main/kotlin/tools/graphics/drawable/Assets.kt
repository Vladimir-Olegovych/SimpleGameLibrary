package tools.graphics.drawable

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture

object Assets {
    val blankWhite = Texture(Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
        setColor(Color.WHITE)
        drawPixel(0, 0)
    })
}