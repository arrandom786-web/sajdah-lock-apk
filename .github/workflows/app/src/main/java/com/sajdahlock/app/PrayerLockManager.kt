package com.sajdahlock.app

import android.content.Context
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.util.Calendar
import java.util.Date

object PrayerLockManager {
    // Shared state toggle
    private var isManuallyUnlockedForWindow = false
    private var lastUnlockedPrayerName: String = ""

    // Default coordinates (User can update in settings)
    var latitude: Double = 24.8607
    var longitude: Double = 67.0011

    fun isLockActive(context: Context): Boolean {
        val currentPrayer = getCurrentActivePrayerWindow() ?: return false
        
        // If the user already verified this prayer session, keep device unlocked
        if (isManuallyUnlockedForWindow && lastUnlockedPrayerName == currentPrayer) {
            return false
        }
        return true
    }

    fun getCurrentActivePrayerWindow(): String? {
        val coordinates = Coordinates(latitude, longitude)
        val date = DateComponents.from(Date())
        val params: CalculationParameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        params.madhab = Madhab.HANAFI

        val prayerTimes = PrayerTimes(coordinates, date, params) ?: return null
        val now = Date()

        // 30-minute lock window starting from each prayer time
        val lockDurationMillis = 30 * 60 * 1000L

        val prayers = listOf(
            "Fajr" to prayerTimes.fajr,
            "Dhuhr" to prayerTimes.dhuhr,
            "Asr" to prayerTimes.asr,
            "Maghrib" to prayerTimes.maghrib,
            "Isha" to prayerTimes.isha
        )

        for ((name, pTime) in prayers) {
            if (pTime != null) {
                val diff = now.time - pTime.time
                if (diff in 0..lockDurationMillis) {
                    return name
                }
            }
        }
        return null
    }

    fun verifyAndUnlock(prayerName: String) {
        isManuallyUnlockedForWindow = true
        lastUnlockedPrayerName = prayerName
    }
}
