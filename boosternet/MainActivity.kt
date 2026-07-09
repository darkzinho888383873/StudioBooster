// DeviceDetector.kt
package boosternet.config

import android.os.Build

object DeviceDetector {
    fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            manufacturer = Build.MANUFACTURER.lowercase(),
            model = Build.MODEL,
            sdkVersion = Build.VERSION.SDK_INT,
            isSamsung = Build.MANUFACTURER.equals("samsung", true),
            isOneUI6 = Build.VERSION.SDK_INT == 34,
            isOneUI7 = Build.VERSION.SDK_INT >= 35
        )
    }
    
    fun optimizeForDevice(): OptimizationLevel {
        val device = getDeviceInfo()
        return when {
            device.isOneUI7 -> OptimizationLevel.ADVANCED
            device.isOneUI6 -> OptimizationLevel.OPTIMIZED
            device.isSamsung -> OptimizationLevel.BASIC
            else -> OptimizationLevel.COMPATIBILITY
        }
    }
}

data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val sdkVersion: Int,
    val isSamsung: Boolean,
    val isOneUI6: Boolean,
    val isOneUI7: Boolean
)

enum class OptimizationLevel {
    COMPATIBILITY,  // Outros dispositivos
    BASIC,          // Samsung antigo
    OPTIMIZED,      // One UI 6.0/6.1
    ADVANCED        // One UI 7.0
}
