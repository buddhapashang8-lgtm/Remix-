package com.example.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShotAnalysis(
    val shotNumber: Int,
    val startTime: Double,
    val endTime: Double,
    val duration: Double,
    val description: String,
    val storyPurpose: String,
    val subjects: List<String> = emptyList(),
    val subjectAppearance: String = "",
    val subjectAction: String = "",
    val objectAction: String = "",
    val environment: String = "",
    val foreground: String = "",
    val midground: String = "",
    val background: String = "",
    val cameraFraming: String = "",
    val cameraAngle: String = "",
    val cameraHeight: String = "",
    val cameraMovement: String = "",
    val subjectMovement: String = "",
    val movementDirection: String = "",
    val movementSpeed: String = "",
    val lighting: String = "",
    val visualStyle: String = "",
    val transition: String = "",
    val audio: String = "",
    val dialogue: String = "",
    val soundEffects: List<String> = emptyList(),
    val emotionalFunction: String = "",
    val continuityNotes: List<String> = emptyList(),
    val importance: Int = 8 // 1-10
)

@JsonClass(generateAdapter = true)
data class ReferenceAnalysis(
    val summary: String,
    val totalDuration: Double,
    val shots: List<ShotAnalysis>,
    val detectedStyle: String,
    val overallPacing: String
)
