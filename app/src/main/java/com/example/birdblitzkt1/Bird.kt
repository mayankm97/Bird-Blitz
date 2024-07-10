package com.example.birdblitzkt1

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.birdblitzkt1.GameView.Companion.screenRatioX
import com.example.birdblitzkt1.GameView.Companion.screenRatioY

class Bird {
    var wasShot = true
    var x = 0
    var y = 0
    var birdCounter = 1
    var bird1: Bitmap
    var bird2: Bitmap
    var bird3: Bitmap
    var bird4: Bitmap
    var speed = 20
    var width: Int
    var height: Int

    constructor(res: Resources) {
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1)
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2)
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3)
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4)

        width = bird1.width
        height = bird1.height
        width /= 6
        height /= 6
        width *= screenRatioX.toInt()
        height *= screenRatioY.toInt()

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false)
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false)
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false)
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false)

        y = -height
    }

    fun getBird(): Bitmap {
        if (birdCounter == 1) {
            birdCounter++
            return bird1
        }
        if (birdCounter == 2) {
            birdCounter++
            return bird2
        }
        if (birdCounter == 3) {
            birdCounter++
            return bird3
        }
        birdCounter = 1
        return bird4
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x+width, y+height)
    }

}