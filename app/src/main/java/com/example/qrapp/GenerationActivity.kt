package com.example.qrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class GenerationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generation)
    }

    fun onGenerate(@Suppress("UNUSED_PARAMETER") view: View) {
        TODO("Implement onGenerate()")
    }
}