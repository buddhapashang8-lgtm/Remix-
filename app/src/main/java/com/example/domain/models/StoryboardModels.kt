package com.example.domain.models

import com.squareup.moshi.JsonClass

enum class ShotGenStatus {
    READY,
    QUEUED,
    GENERATING,
    COMPLETED,
    FAILED
}

@JsonClass(generateAdapter = true)
data class StoryboardShot(
    val shotNumber: Int,
    val duration: Double,
    val storyPurpose: String,
    val visualDescription: String,
    val character: String = "CHARACTER_C01",
    val action: String,
    val objectBehavior: String,
    val environment: String = "ENV_E01",
    val cameraFraming: String,
    val cameraAngle: String,
    val cameraMovement: String,
    val subjectMovement: String,
    val lighting: String,
    val transition: String,
    val audio: String,
    val continuityRequirements: List<String> = emptyList(),
    val generationPrompt: String,
    val negativeConstraints: List<String> = emptyList(),
    val status: ShotGenStatus = ShotGenStatus.READY,
    val videoPreviewUrl: String? = null,
    val thumbnailPreviewUrl: String? = null,
    val progress: Float = 0f,
    val errorMessage: String? = null
)

@JsonClass(generateAdapter = true)
data class Storyboard(
    val title: String,
    val totalDuration: Double,
    val shots: List<StoryboardShot>
)
