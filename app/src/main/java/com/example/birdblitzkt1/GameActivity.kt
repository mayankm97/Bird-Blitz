package com.example.birdblitzkt1

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class GameActivity : AppCompatActivity() {

    var gameView: GameView ?= null
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        gameView = GameView(this, point.x, point.y)
        Toast.makeText(this, "${gameView!!.screenX}, ${gameView!!.screenY}", Toast.LENGTH_SHORT).show()

        setContentView(gameView)

    }
    override fun onPause() {
        super.onPause()
        gameView?.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView?.resume()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun enableEdgeToEdge() {
        window.setDecorFitsSystemWindows(false)
        WindowCompat.getInsetsController(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}