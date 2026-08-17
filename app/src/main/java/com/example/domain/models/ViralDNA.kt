package com.example.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HookDNA(
    val description: String = "",
    val mechanism: String = "",
    val timing: String = ""
)

@JsonClass(generateAdapter = true)
data class StoryDNA(
    val setup: String = "",
    val problem: String = "",
    val anticipation: String = "",
    val surprise: String = "",
    val reveal: String = "",
    val proof: String = "",
    val payoff: String = "",
    val ending: String = ""
)

@JsonClass(generateAdapter = true)
data class CameraDNA(
    val cameraStyle: String = "",
    val framingPattern: String = "",
    val movementPattern: String = "",
    val cameraHeight: String = "",
    val subjectDistance: String = "",
    val trackingStyle: String = "",
    val zoomBehavior: String = "",
    val stabilization: String = "",
    val cutFrequency: String = ""
)

@JsonClass(generateAdapter = true)
data class MotionDNA(
    val subjectTrajectory: List<String> = emptyList(),
    val velocityPattern: List<String> = emptyList(),
    val importantMotionEvents: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PacingSegment(
    val label: String,
    val startSec: Double,
    val endSec: Double,
    val energyLevel: String // LOW, MEDIUM, PEAK, RESOLUTION
)

@JsonClass(generateAdapter = true)
data class PacingDNA(
    val totalDuration: Double = 0.0,
    val segments: List<PacingSegment> = emptyList()
)

@JsonClass(generateAdapter = true)
data class VisualDNA(
    val realism: String = "",
    val lighting: String = "",
    val colorCharacteristics: String = "",
    val environmentStyle: String = "",
    val captureStyle: String = ""
)

@JsonClass(generateAdapter = true)
data class AudioDNA(
    val music: String = "",
    val rhythm: String = "",
    val ambience: String = "",
    val soundEvents: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ViralDNA(
    val coreConcept: String = "",
    val literalConcept: String = "",
    val abstractConcept: String = "",
    val hook: HookDNA = HookDNA(),
    val story: StoryDNA = StoryDNA(),
    val storyStructure: List<String> = emptyList(),
    val cameraDNA: CameraDNA = CameraDNA(),
    val motionDNA: MotionDNA = MotionDNA(),
    val pacingDNA: PacingDNA = PacingDNA(),
    val visualDNA: VisualDNA = VisualDNA(),
    val audioDNA: AudioDNA = AudioDNA(),
    val continuityRules: List<String> = emptyList(),
    val preserveCandidates: List<String> = emptyList(),
    val replaceCandidates: List<String> = emptyList()
)
