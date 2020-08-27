package org.opencv.samples.facedetect

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

public class Helper {

    public fun convertBitmapToByteBuffer(bitmap: Bitmap,modelInputSize: Int): ByteBuffer {
        // Specify the size of the byteBuffer
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        // Calculate the number of pixels in the image
        val pixels = IntArray(128 * 128)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        // Loop through all the pixels and save them into the buffer
        for (i in 0 until 128) {
            for (j in 0 until 128) {
                val pixelVal = pixels[pixel++]
                // Do note that the method to add pixels to byteBuffer is different for quantized models over normal tflite models
                byteBuffer.put((pixelVal shr 16 and 0xFF).toByte())
                byteBuffer.put((pixelVal shr 8 and 0xFF).toByte())
                byteBuffer.put((pixelVal and 0xFF).toByte())
            }
        }

        // Recycle the bitmap to save memory
        bitmap.recycle()
        return byteBuffer
    }
}