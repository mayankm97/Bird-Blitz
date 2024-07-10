package com.example.birdblitzkt1

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background(screenX: Int, screenY: Int, res: Resources) {
    var x:Int = 0
    var y: Int = 0

    var background: Bitmap = BitmapFactory.decodeResource(res, R.drawable.background)
    init {
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }
}