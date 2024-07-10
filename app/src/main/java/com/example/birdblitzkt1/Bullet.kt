package com.example.birdblitzkt1

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.birdblitzkt1.GameView.Companion.screenRatioX
import com.example.birdblitzkt1.GameView.Companion.screenRatioY

class Bullet(res: Resources) {
    var x = 0
    var y = 0
    var bullet = BitmapFactory.decodeResource(res, R.drawable.bullet)
    var width = 0
    var height = 0
    init {
        width = bullet.width
        height = bullet.height

        width /= 4
        height /= 4
        width *= screenRatioX.toInt()
        height *= screenRatioY.toInt()

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false)
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x+width, y+height)
    }
}