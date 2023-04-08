package com.example.qrapp

import android.Manifest.permission.CAMERA
import android.Manifest.permission.VIBRATE
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qrapp.ContentType.*
import eu.livotov.labs.android.camview.ScannerLiveView


class ScannerActivity : AppCompatActivity() {
    private lateinit var camView: ScannerLiveView
    private lateinit var resultWrapper: ConstraintLayout
    private lateinit var resultTextView: TextView
    private lateinit var actionBtn: Button
    private lateinit var contentType: ContentType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        if (!hasPermissions()) requestPermissions()

        resultWrapper = findViewById(R.id.resultWrapper)
        resultTextView = findViewById(R.id.resultTextView)
        actionBtn = findViewById(R.id.actionBtn)
        camView = findViewById(R.id.camView)

        if (savedInstanceState != null) {
            val data = savedInstanceState.getString("data")
            if (data != null) setScanResult(data)
        }

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
                if (data == null) return
                setScanResult(data)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("data", resultTextView.text.toString())
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

    private fun setScanResult(data: String) {
        resultWrapper.visibility = VISIBLE
        resultTextView.text = data
        if (URLUtil.isValidUrl(data)) {
            actionBtn.text = resources.getString(R.string.globe)
            contentType = HYPER_LINK
        } else {
            actionBtn.text = resources.getString(R.string.clipboard)
            contentType = PLAIN_TEXT
        }
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

    fun onClose(@Suppress("UNUSED_PARAMETER") view: View) {
        resultWrapper.visibility = INVISIBLE
    }

    fun onAction(@Suppress("UNUSED_PARAMETER") view: View) {
        val data = resultTextView.text.toString()
        when (contentType) {
            PLAIN_TEXT -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", data)
                clipboard.setPrimaryClip(clip)
            }
            HYPER_LINK -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data))
                startActivity(browserIntent)
            }
        }
    }
}