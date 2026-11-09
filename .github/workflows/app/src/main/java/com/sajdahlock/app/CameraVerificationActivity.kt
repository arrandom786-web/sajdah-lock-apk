package com.sajdahlock.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LockScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simple programmatic UI layout
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setBackgroundColor(0xFF0F172A.toInt()) // Slate-900
            setPadding(48, 48, 48, 48)
        }

        val title = TextView(this).apply {
            text = "Salah Time Lockdown"
            textSize = 24f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = android.view.Gravity.CENTER
        }

        val prayerNotice = TextView(this).apply {
            val current = PrayerLockManager.getCurrentActivePrayerWindow() ?: "Prayer"
            text = "It is time for $current Prayer.\nApps are restricted until you scan your prayer mat."
            textSize = 15f
            setTextColor(0xFF94A3B8.toInt())
            gravity = android.view.Gravity.CENTER
            setPadding(0, 24, 0, 48)
        }

        val verifyBtn = Button(this).apply {
            text = "Scan Prayer Mat to Unlock"
            setBackgroundColor(0xFF059669.toInt()) // Emerald-600
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener {
                startActivity(Intent(this@LockScreenActivity, CameraVerificationActivity::class.java))
                finish()
            }
        }

        layout.addView(title)
        layout.addView(prayerNotice)
        layout.addView(verifyBtn)

        setContentView(layout)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Prevent dismissal by back button during prayer window
        if (!PrayerLockManager.isLockActive(this)) {
            super.onBackPressed()
        }
    }
}
