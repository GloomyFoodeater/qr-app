package com.example.qrapp

import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder

class QrConverter {
    companion object {
        fun stringToBitmap(data: String, width: Int, height: Int): Bitmap {
            val dimension = 3 * Integer.min(width, height) / 4
            val encoder = QRGEncoder(data, QRGContents.Type.TEXT, dimension)
            return encoder.getBitmap(0)
        }
    }
}