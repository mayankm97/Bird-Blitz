package com.example.birdblitzkt1

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {

    private var isMute: Boolean = false     // the default value to be set later by sharedpreference will be the actual one.

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable edge-to-edge display
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.play).setOnClickListener(View.OnClickListener {
            startActivity(Intent (this, GameActivity::class.java))
        })
        val highScoreTxt = findViewById<TextView>(R.id.highScoreTxt)

        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        highScoreTxt.setText("High Score: " + prefs.getInt("highScore", 0))

        isMute = prefs.getBoolean("isMute", false)       // the defVal is final value for isMute

        Toast.makeText(this, "$isMute", Toast.LENGTH_SHORT).show()
        val volCtrl = findViewById<ImageView>(R.id.volumeCtrl)

        if (isMute)
            volCtrl.setImageResource(R.drawable.baseline_volume_off_24)
        else
            volCtrl.setImageResource(R.drawable.baseline_volume_up_24)

        volCtrl.setOnClickListener(View.OnClickListener {
            isMute = !isMute
            if (isMute as Boolean)
                volCtrl.setImageResource(R.drawable.baseline_volume_off_24)
            else
                volCtrl.setImageResource(R.drawable.baseline_volume_up_24)

            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        })
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public fun enableEdgeToEdge() {
        window.setDecorFitsSystemWindows(false)
        WindowCompat.getInsetsController(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
