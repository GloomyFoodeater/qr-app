package com.example.qrapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.qrapp.QrConverter.Companion as Qr

class GenerationActivity : AppCompatActivity() {
    private lateinit var qrEdit: EditText
    private lateinit var qrImageView: ImageView
    private var savedInstanceState: Bundle? = null // Obtained in creation, used in focus changing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generation)
        this.savedInstanceState = savedInstanceState
        qrEdit = findViewById(R.id.qrEdit)
        qrImageView = findViewById(R.id.qrImageView)
    }

    // Handler to event when image view sizes are initialized
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && savedInstanceState != null) {
            val data = savedInstanceState!!.getString("data")!!
            if (data != "") {
                qrEdit.setText(data)
                val image = Qr.stringToBitmap(data, qrImageView.width, qrImageView.height)
                qrImageView.setImageBitmap(image)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onGenerate(@Suppress("UNUSED_PARAMETER") view: View) {
        val data = qrEdit.text.toString()
        if (data != "") {
            val image = Qr.stringToBitmap(data, qrImageView.width, qrImageView.height)
            qrImageView.setImageBitmap(image)
        } else {
            qrImageView.setImageBitmap(null)
            val blinkAnimation = AlphaAnimation(0.0f, 1.0f)
            blinkAnimation.duration = 200
            blinkAnimation.repeatCount = 1
            qrEdit.startAnimation(blinkAnimation)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("data", qrEdit.text.toString())
    }
}