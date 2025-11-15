// ImageUtils.kt
package com.android.example.pruebaprueba.data.service.mindee

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