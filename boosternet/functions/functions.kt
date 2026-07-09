// functions/functions.kt
package functions

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

// ==========================================
// RAM BOOSTER
// ==========================================

object RamBooster {
    
    data class RamInfo(
        val total: Long,
        val available: Long,
        val freed: Long
    )
    
    fun boost(context: Context): RamInfo {
        val before = getAvailableRam(context)
        
        if (Build.VERSION.SDK_INT >= 35) {
            advancedClean(context)
        } else {
            standardClean(context)
        }
        
        val after = getAvailableRam(context)
        
        return RamInfo(
            total = getTotalRam(context),
            available = after,
            freed = after - before
        )
    }
    
    private fun advancedClean(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.runningAppProcesses?.forEach { process ->
            if (process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                am.killBackgroundProcesses(process.processName)
            }
        }
    }
    
    private fun standardClean(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.runningAppProcesses?.forEach { process ->
            if (process.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                am.killBackgroundProcesses(process.processName)
            }
        }
        System.gc()
    }
    
    fun getAvailableRam(context: Context): Long {
        val memInfo = ActivityManager.MemoryInfo()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.getMemoryInfo(memInfo)
        return memInfo.availMem
    }
    
    fun getTotalRam(context: Context): Long {
        val memInfo = ActivityManager.MemoryInfo()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.getMemoryInfo(memInfo)
        return memInfo.totalMem
    }
}

// ==========================================
// CPU MANAGER
// ==========================================

object CpuManager {
    
    fun setPerformanceMode() {
        if (Build.VERSION.SDK_INT >= 35) {
            exec("echo performance > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
        } else {
            exec("echo schedutil > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
        }
    }
    
    fun setPowersaveMode() {
        exec("echo powersave > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
    }
    
    private fun exec(command: String): Boolean {
        return try {
            Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            true
        } catch (e: Exception) {
            false
        }
    }
}

// ==========================================
// GPU TUNER
// ==========================================

object GpuTuner {
    
    fun setMaxPerformance() {
        if (Build.VERSION.SDK_INT >= 35) {
            exec("echo 1 > /sys/class/kgsl/kgsl-3d0/max_gpuclk")
        }
    }
    
    fun setBalanced() {
        exec("echo 0 > /sys/class/kgsl/kgsl-3d0/max_gpuclk")
    }
    
    private fun exec(command: String): Boolean {
        return try {
            Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            true
        } catch (e: Exception) {
            false
        }
    }
}

// ==========================================
// THERMAL CONTROLLER
// ==========================================

object ThermalController {
    
    fun getTemperature(): Float {
        return try {
            val input = Runtime.getRuntime().exec("cat /sys/class/thermal/thermal_zone0/temp")
            val reader = input.inputStream.bufferedReader()
            val temp = reader.readLine().toFloat() / 1000f
            reader.close()
            temp
        } catch (e: Exception) {
            0f
        }
    }
    
    fun coolDown() {
        CpuManager.setPowersaveMode()
        GpuTuner.setBalanced()
    }
}

// ==========================================
// NETWORK OPTIMIZER
// ==========================================

object NetworkOptimizer {
    
    fun optimizeForGaming() {
        exec("echo 1 > /proc/sys/net/ipv4/tcp_low_latency")
    }
    
    fun optimizeForStreaming() {
        exec("echo 0 > /proc/sys/net/ipv4/tcp_low_latency")
        exec("echo 1 > /proc/sys/net/ipv4/tcp_window_scaling")
    }
    
    private fun exec(command: String): Boolean {
        return try {
            Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            true
        } catch (e: Exception) {
            false
        }
    }
}

// ==========================================
// APP FREEZER
// ==========================================

object AppFreezer {
    
    fun freeze(context: Context, packageName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= 35) {
                val pm = context.packageManager
                pm.setApplicationEnabledSetting(
                    packageName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    0
                )
            } else {
                val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                am.killBackgroundProcesses(packageName)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun unfreeze(context: Context, packageName: String): Boolean {
        return try {
            val pm = context.packageManager
            pm.setApplicationEnabledSetting(
                packageName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                0
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}

// ==========================================
// BOOSTER COMPLETO
// ==========================================

object FullBooster {
    
    fun boostAll(context: Context) {
        RamBooster.boost(context)
        CpuManager.setPerformanceMode()
        GpuTuner.setMaxPerformance()
        NetworkOptimizer.optimizeForGaming()
    }
}
