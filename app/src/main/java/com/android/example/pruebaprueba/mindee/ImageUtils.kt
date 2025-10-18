// ImageUtils.kt
package com.android.example.pruebaprueba.mindeeocr

import android.content.Context
import java.io.File

object ImageUtils {
    fun getOutputDirectory(context: Context): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, "mindee_ocr").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }
}