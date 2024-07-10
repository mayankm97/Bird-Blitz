package com.example.birdblitzkt1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.SoundPool
import android.view.MotionEvent
import android.view.SurfaceView
import kotlinx.coroutines.Runnable
import java.util.Random

class GameView(activity: GameActivity, screenX: Int, screenY: Int) : SurfaceView(activity), Runnable {
    private var th: Thread
    val screenX: Int
    val screenY: Int
    var score = 0
    private var isPlaying: Boolean? = null
    private val paint: Paint
    private var background1: Background = Background(screenX, screenY, resources)
    private var background2: Background = Background(screenX, screenY, resources)
    private var flight: Flight
    var bullets: MutableList<Bullet>
    private var birds: Array<Bird>
    private var random: kotlin.random.Random
    private var activity: GameActivity
    var isGameOver: Boolean = false
    private var sharedPrefs: SharedPreferences
    private val soundPool: SoundPool
    private val sound: Int

    init {
        this.activity = activity
        sharedPrefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE)

        val audioAttributes = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_GAME).build()
        soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).build()
        sound = soundPool.load(activity, R.raw.shoot, 1)

        th = Thread(this)
        paint = Paint()
        paint.textSize = 128f
        paint.setColor(Color.WHITE)

        this.screenX = screenX
        this.screenY = screenY
        background2.x = screenX

        screenRatioX = 2186f / screenX
        screenRatioY = 1080f / screenY

        bullets = mutableListOf()
        flight = Flight(this, screenY, resources)
        birds = Array(2) { Bird(resources) }
        // No of objects made = no of birds that will appear on your screen.
        /*{ Bird(resources)}: This lambda function is called for each index of the array,
         where it(if used, if not it internally does) represents the current index (from 0 to 3 in this case).
         above line in kotlin is corresponding to below code of java.
         birds = new Bird[4]
         for i = 0 -> 4 Bird bird = new Bird(getResources) and birds[i] = bird */

        random = kotlin.random.Random
    }

    override fun run() {
        while (isPlaying == true) {
            update()
            draw()
            sleep()
        }
    }

    fun update() {
        background1.x -= 10 * screenRatioX.toInt()
        background2.x -= 10 * screenRatioX.toInt()

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX
        }

        if (flight.isGoingUp) flight.y -= 30 * screenRatioY.toInt()
        else flight.y += 30 * screenRatioY.toInt()
        if (flight.y < 0) flight.y = 0
        if (flight.y > screenY - flight.height) flight.y = screenY - flight.height

        var trash = mutableListOf<Bullet>()

        bullets.forEach {
            if (it.x > screenX) trash.add(it)
            it.x += 50 * screenRatioX.toInt()

            birds.forEach { bird ->
                if (Rect.intersects(bird.getCollisionShape(), it.getCollisionShape())) {
                    score++
                    bird.x = -500
                    it.x = screenX + 500
                    bird.wasShot = true
                }
            }
        }
        trash.forEach {
            bullets.remove(it)
        }
        birds.forEach { bird ->
            bird.x -= bird.speed
            if (bird.x + bird.width < 0) {

                if (!bird.wasShot) {
                    isGameOver = true
                    return
                }

                val bound = 30 * screenRatioX.toInt()
                bird.speed = random.nextInt(bound)

                if (bird.speed < 10 * screenRatioX) {
                    bird.speed = 10 * screenRatioX.toInt()
                }
                bird.x = screenX
                bird.y = random.nextInt(screenY - bird.height)

                bird.wasShot = false
            }

            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true
                return
            }
        }

    }

    fun draw() {
        if (holder.surface.isValid) {
            val canvas: Canvas = holder.lockCanvas()
            // Draw the first background
            canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
            // Draw the second background
            canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

            birds.forEach {
                canvas.drawBitmap(it.getBird(), it.x.toFloat(), it.y.toFloat(), paint)
            }
            canvas.drawText("" + score, screenX/2f, 164f, paint)
            if (isGameOver) {
                isPlaying = false
                canvas.drawBitmap(flight.getDed(), flight.x.toFloat(), flight.y.toFloat(), paint)
                holder.unlockCanvasAndPost(canvas)
                saveIfHighScore()
                waitBeforeExiting()
                return
            }

            canvas.drawBitmap(flight.getFlight(), flight.x.toFloat(), flight.y.toFloat(), paint)
            bullets.forEach {
                canvas.drawBitmap(it.bullet, it.x.toFloat(), it.y.toFloat(), paint)
            }
            // Unlock the canvas and post the update
            holder.unlockCanvasAndPost(canvas)

        }
    }

    private fun waitBeforeExiting() {
        Thread.sleep(3000)
        // The context i.e. gameActivity here represents the activity that created the GameView, which provides the
        // necessary environment to start new activities
        activity.startActivity(Intent(activity, MainActivity::class.java))
        activity.finish() // finish() is called to close the current activity,
            // ensuring that it is removed from the activity stack. This ensures that when the user navigates to GameActivity,
            // the current activity is closed and won't be accessible through the back button.
    }

    private fun saveIfHighScore() {
        if (sharedPrefs.getInt("highScore", 0) < score) {
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putInt("highScore", score)
            editor.apply()
            // To put, we use put and apply, to get, just 'get' using key.
        }
    }

    fun sleep() {
        Thread.sleep(17)
    }

    fun resume() {
        isPlaying = true
        th.start()
    }

    fun pause() {
        th.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> if (event.x < screenX / 2) flight.isGoingUp = true
            MotionEvent.ACTION_UP -> {
                flight.isGoingUp = false
                if (event.x > screenX / 2) {
                    flight.toShoot++
                }
            }
        }
        return true
    }

    fun newBullet() {
        if (!sharedPrefs.getBoolean("isMute", false)) {
            soundPool.play(sound, 1F, 1F, 0, 0, 1F)
        }
        val bullet = Bullet(resources)
        bullet.x = flight.x + flight.width
        bullet.y = flight.y + (flight.height / 2)
        bullets.add(bullet)
    }

    companion object {
        var screenRatioX: Float = 0.0f
        var screenRatioY: Float = 0.0f
    }
}