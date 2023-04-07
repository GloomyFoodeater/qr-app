package com.example.qrapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onGenerate(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, GenerationActivity::class.java)
        startActivity(intent)
    }
    fun onScan(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, ScannerActivity::class.java)
        startActivity(intent)
    }
}