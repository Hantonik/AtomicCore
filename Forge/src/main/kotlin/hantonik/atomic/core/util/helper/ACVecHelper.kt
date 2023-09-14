package hantonik.atomic.core.util.helper

import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

object ACVecHelper {
    fun rotate(vec: Vec3, yaw: Double, pitch: Double, roll: Double): Vec3 {
        val yawRad = Math.toRadians(yaw)
        val pitchRad = Math.toRadians(pitch)
        val rollRad = Math.toRadians(roll)

        val xPos = vec.x * cos(yawRad) * cos(pitchRad) + vec.z * (cos(yawRad) * sin(pitchRad) * sin(rollRad) - sin(yawRad) * cos(rollRad)) + vec.y * (cos(yawRad) * sin(pitchRad) * cos(rollRad) + sin(yawRad) * sin(rollRad));
        val zPos = vec.x * sin(yawRad) * cos(pitchRad) + vec.z * (sin(yawRad) * sin(pitchRad) * sin(rollRad) + cos(yawRad) * cos(rollRad)) + vec.y * (sin(yawRad) * sin(pitchRad) * cos(rollRad) - cos(yawRad) * sin(rollRad))
        val yPos = -vec.x * sin(pitchRad) + vec.z * cos(pitchRad) * sin(rollRad) + vec.y * cos(pitchRad) * cos(rollRad)

        return Vec3(xPos, yPos, zPos)
    }
}