package tools.physics

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef

fun Body.setSensorRadius(userData: Any? = null,
                         radius: Float) {
    val fDef = FixtureDef()
    fDef.shape = CircleShape().also { it.radius = radius }
    fDef.isSensor = true
    this.createFixture(fDef).userData = userData
}