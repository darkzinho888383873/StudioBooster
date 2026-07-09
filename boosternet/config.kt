// boosternet/config.kt
package boosternet

import android.os.Build

object DeviceConfig {
    
    data class Device(
        val brand: String,
        val model: String,
        val sdk: Int,
        val isSamsung: Boolean,
        val oneUI: String
    )
    
    fun detect(): Device {
        return Device(
            brand = Build.BRAND,
            model = Build.MODEL,
            sdk = Build.VERSION.SDK_INT,
            isSamsung = Build.BRAND.equals("samsung", true),
            oneUI = when {
                Build.VERSION.SDK_INT >= 35 -> "7.0"
                Build.VERSION.SDK_INT == 34 -> "6.0 ou 6.1"
                else -> "Desconhecida"
            }
        )
    }
}
