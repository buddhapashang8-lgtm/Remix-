package com.example.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReferenceVideo(
    val uri: String,
    val title: String,
    val durationSeconds: Double,
    val resolution: String = "1080x1920",
    val aspectRatio: String = "9:16",
    val fileSizeFormatted: String = "18.4 MB",
    val isPreset: Boolean = false,
    val previewThumbnailUrl: String? = null,
    val description: String = ""
)
