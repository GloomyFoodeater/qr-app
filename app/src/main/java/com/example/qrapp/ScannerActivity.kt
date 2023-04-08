package com.example.qrapp

import android.Manifest.permission.CAMERA
import android.Manifest.permission.VIBRATE
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import eu.livotov.labs.android.camview.ScannerLiveView

class ScannerActivity : AppCompatActivity() {
    private lateinit var camView: ScannerLiveView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        if (!hasPermissions()) requestPermissions()

        camView = findViewById(R.id.camView)
        camView.isPlaySound = false
        camView.scannerViewEventListener = object : ScannerLiveView.ScannerViewEventListener {
            override fun onScannerStarted(scanner: ScannerLiveView?) {}

            override fun onScannerStopped(scanner: ScannerLiveView?) {}

            override fun onScannerError(err: Throwable?) {
                Toast.makeText(
                    this@ScannerActivity,
                    "Scanner Error: ${err?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCodeScanned(data: String?) {
                Toast.makeText(this@ScannerActivity, data, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        camView.decoder = QrOnlyDecoder()
        camView.startScanner()
    }

    override fun onPause() {
        camView.stopScanner()
        super.onPause()
    }

    private fun hasPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(applicationContext, CAMERA)
        val vibratePermission = ContextCompat.checkSelfPermission(applicationContext, VIBRATE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                vibratePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val permissionRequestCode = 200
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA, VIBRATE), permissionRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            val isCameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val isVibrationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
            if (!(isCameraAccepted && isVibrationAccepted)) {
                Toast.makeText(
                    this,
                    "Permission Denied \n You cannot use app without providing permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}