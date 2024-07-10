package com.example.birdblitzkt1

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.birdblitzkt1.GameView.Companion.screenRatioX
import com.example.birdblitzkt1.GameView.Companion.screenRatioY

class Flight(gameView: GameView, screenY: Int, res: Resources) {
    var height = 0
    var width = 0
    var isGoingUp: Boolean = false
    var x = 0
    var y = 0
    var wingCounter = 0
    var toShoot = 0
    var shootCounter = 1
    var gameView: GameView ?= null

    var flight1: Bitmap = BitmapFactory.decodeResource(res, R.drawable.fly1)
    var flight2: Bitmap = BitmapFactory.decodeResource(res, R.drawable.fly2)
    var shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1)
    var shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2)
    var shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3)
    var shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4)
    var shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5)
    var dead = BitmapFactory.decodeResource(res, R.drawable.dead)

    init {

        this.gameView = gameView
        width = flight1.width
        height = flight1.height

        width /= 4
        height /= 4
        width *= screenRatioX.toInt()
        height *= screenRatioY.toInt()

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false)
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false)
        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false)
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false)
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false)
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false)
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false)
        dead = Bitmap.createScaledBitmap(dead, width, height, false)

        y = screenY / 2
        x = 64 * screenRatioX.toInt()
    }
    fun getFlight(): Bitmap {
        if (toShoot != 0) {
            if (shootCounter == 1) {
                shootCounter++
                return shoot1
            }
            if (shootCounter == 2) {
                shootCounter++
                return shoot2
            }
            if (shootCounter == 3) {
                shootCounter++
                return shoot3
            }
            if (shootCounter == 4) {
                shootCounter++
                return shoot4
            }
            shootCounter = 1
            toShoot--
            gameView!!.newBullet()
            return shoot5
        }

        if (wingCounter == 0) {
            wingCounter++
            return flight1
        }
        wingCounter--
        return flight2
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x+width, y+height)
    }

    fun getDed(): Bitmap {
        return dead
    }
}