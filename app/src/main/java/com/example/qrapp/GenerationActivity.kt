package com.example.qrapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import java.lang.Integer.min

class GenerationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generation)
    }

    fun onGenerate(@Suppress("UNUSED_PARAMETER") view: View) {
        val sourceEdit = findViewById<EditText>(R.id.sourceEdit)
        val data = sourceEdit.text.toString()
        val generatedImageView = findViewById<ImageView>(R.id.generatedImageView)
        val width = generatedImageView.width
        val height = generatedImageView.height
        val dimension = 3 * min(width, height) / 4
        val encoder = QRGEncoder(data, QRGContents.Type.TEXT, dimension)
        val image = encoder.getBitmap(0)
        generatedImageView.setImageBitmap(image)
    }
}