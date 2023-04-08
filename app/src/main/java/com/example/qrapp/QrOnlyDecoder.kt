package com.example.qrapp

import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import eu.livotov.labs.android.camview.scanner.decoder.BarcodeDecoder
import eu.livotov.labs.android.camview.scanner.decoder.zxing.PlanarRotatedYUVLuminanceSource
import java.util.*

// See eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder
// Changed hardcoded formats in init section
class QrOnlyDecoder : BarcodeDecoder {
    private var reader: MultiFormatReader = MultiFormatReader()
    private var scanAreaPercent = 0.7

    init {
        reader = MultiFormatReader()

        val hints: MutableMap<DecodeHintType, Any?> = EnumMap(DecodeHintType::class.java)
        val decodeFormats: Collection<BarcodeFormat> = EnumSet.of(BarcodeFormat.QR_CODE)
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats

        hints[DecodeHintType.CHARACTER_SET] = "utf-8"
        hints[DecodeHintType.TRY_HARDER] = true

        reader.setHints(hints)
    }

    override fun setScanAreaPercent(scanAreaPercent: Double) {
        require(!(scanAreaPercent < 0.1 || scanAreaPercent > 1.0)) { "Scan area percent must be between 0.1 (10%) to 1.0 (100%). Specified value was $scanAreaPercent" }
        this.scanAreaPercent = scanAreaPercent
    }

    @Suppress("EMPTY_CATCH_BLOCK")
    override fun decode(image: ByteArray, width: Int, height: Int): String? {
        var result: Result?
        val scanWidth = (width * scanAreaPercent).toInt()
        val scanHeight = (height * scanAreaPercent).toInt()
        val scanAreaLeft = width / 2 - scanWidth / 2
        val scanAreaRight = width / 2 + scanWidth / 2
        val scanAreaTop = height / 2 - scanHeight / 2
        val scanAreaBottom = height / 2 + scanHeight / 2

        // First try image as is
        try {
            val bitmap = BinaryBitmap(
                HybridBinarizer(
                    PlanarYUVLuminanceSource(
                        image,
                        width,
                        height,
                        scanAreaLeft,
                        scanAreaTop,
                        scanAreaRight,
                        scanAreaBottom,
                        true
                    )
                )
            )
            result = reader.decodeWithState(bitmap)
            if (result != null) {
                return result.text
            }
        }  catch (err: Throwable) {
            // Ignore
        } finally {
            reader.reset()
        }

        // Then try it 90 degrees rotated (works for 1D codes)
        try {
            val bitmap = BinaryBitmap(
                HybridBinarizer(
                    PlanarRotatedYUVLuminanceSource(
                        image,
                        width,
                        height,
                        scanAreaLeft,
                        scanAreaTop,
                        scanAreaRight,
                        scanAreaBottom,
                        true
                    )
                )
            )
            result = reader.decodeWithState(bitmap)
            if (result != null) {
                return result.text
            }
        } catch (re: Throwable) {
            // Ignore
        } finally {
            reader.reset()
        }
        return null
    }
}