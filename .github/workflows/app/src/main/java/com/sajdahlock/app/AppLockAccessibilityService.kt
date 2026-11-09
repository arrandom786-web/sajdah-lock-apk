package com.sajdahlock.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class AppLockAccessibilityService : AccessibilityService() {

    // Common distraction applications to intercept
    private val blacklistedApps = setOf(
        "com.instagram.android",
        "com.zhiliaoapp.musically",       // TikTok
        "com.google.android.youtube",
        "com.facebook.katana",
        "com.twitter.android",
        "com.netflix.mediaclient",
        "com.snapchat.android",
        "com.valvesoftware.android.steam.community"
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val pkg = event.packageName?.toString() ?: return

            // Never block the locker app itself or phone dialer
            if (pkg == packageName || pkg.contains("dialer") || pkg.contains("telecom")) {
                return
            }

            if (PrayerLockManager.isLockActive(this) && blacklistedApps.contains(pkg)) {
                launchLockOverlay()
            }
        }
    }

    private fun launchLockOverlay() {
        val intent = Intent(this, LockScreenActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {}
}
