package com.example.domain.models

import com.squareup.moshi.JsonClass

enum class RemixControlMode {
    PRESERVE,
    ADAPT,
    REPLACE
}

@JsonClass(generateAdapter = true)
data class RemixSettings(
    val userPrompt: String = "",
    val controls: Map<String, RemixControlMode> = defaultDimensionControls()
)

fun defaultDimensionControls(): Map<String, RemixControlMode> = mapOf(
    "Core Story Structure" to RemixControlMode.PRESERVE,
    "Hook Mechanism" to RemixControlMode.PRESERVE,
    "Setup & Build-up" to RemixControlMode.ADAPT,
    "Problem / Obstacle" to RemixControlMode.ADAPT,
    "Surprise Reveal" to RemixControlMode.PRESERVE,
    "Final Payoff" to RemixControlMode.PRESERVE,
    "Shot Timing & Count" to RemixControlMode.PRESERVE,
    "Camera Movement" to RemixControlMode.PRESERVE,
    "Camera Framing" to RemixControlMode.PRESERVE,
    "Subject Movement Pattern" to RemixControlMode.PRESERVE,
    "Transformation Timing" to RemixControlMode.PRESERVE,
    "Visual Realism" to RemixControlMode.PRESERVE,
    "Character Role" to RemixControlMode.PRESERVE,
    "Character Appearance" to RemixControlMode.REPLACE,
    "Environment / Location" to RemixControlMode.REPLACE,
    "Primary Vehicle / Object" to RemixControlMode.REPLACE,
    "Weather & Atmosphere" to RemixControlMode.REPLACE,
    "Lighting Scheme" to RemixControlMode.ADAPT,
    "Audio Rhythm" to RemixControlMode.PRESERVE,
    "Ending Resolution" to RemixControlMode.ADAPT
)

@JsonClass(generateAdapter = true)
data class StructuralDiff(
    val category: String,
    val referenceElement: String,
    val remixedElement: String,
    val mode: RemixControlMode,
    val rationale: String
)

@JsonClass(generateAdapter = true)
data class RemixConcept(
    val title: String = "",
    val oneLineConcept: String = "",
    val fullConcept: String = "",
    val newHook: String = "",
    val newSetup: String = "",
    val newProblem: String = "",
    val newAnticipation: String = "",
    val newSurprise: String = "",
    val newReveal: String = "",
    val newProof: String = "",
    val newPayoff: String = "",
    val newEnding: String = "",
    val preservedElements: List<String> = emptyList(),
    val adaptedElements: List<String> = emptyList(),
    val replacedElements: List<String> = emptyList(),
    val structuralDiffs: List<StructuralDiff> = emptyList(),
    val reasoningSummary: String = "",
    val continuityRequirements: List<String> = emptyList(),
    val shotsSummary: List<String> = emptyList()
)
