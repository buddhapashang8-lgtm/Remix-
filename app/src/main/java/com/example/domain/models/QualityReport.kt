package com.example.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QualityMetric(
    val name: String,
    val score: Int, // 0-100
    val weight: Float = 1.0f,
    val status: String // EXCELLENT, GOOD, WARNING, DEFECT
)

@JsonClass(generateAdapter = true)
data class QualityReport(
    val characterConsistency: Int = 94,
    val vehicleConsistency: Int = 91,
    val environmentConsistency: Int = 96,
    val actionAccuracy: Int = 89,
    val cameraAccuracy: Int = 92,
    val motionAccuracy: Int = 90,
    val transformationAccuracy: Int = 87,
    val temporalAccuracy: Int = 95,
    val visualArtifacts: Int = 88,
    val overallContinuity: Int = 93,
    val critiqueNotes: List<String> = emptyList(),
    val flaggedShotNumbers: List<Int> = emptyList(),
    val shotFixSuggestions: Map<String, String> = emptyMap(),
    val evaluatedAt: Long = System.currentTimeMillis()
)
