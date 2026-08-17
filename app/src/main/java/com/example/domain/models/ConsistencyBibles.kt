package com.example.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterBible(
    val id: String = "CHARACTER_C01",
    val role: String = "Protagonist",
    val ageRange: String = "",
    val appearance: String = "",
    val face: String = "",
    val hair: String = "",
    val facialHair: String = "",
    val bodyType: String = "",
    val wardrobe: String = "",
    val footwear: String = "",
    val accessories: String = "",
    val continuityRules: List<String> = emptyList(),
    val isLocked: Boolean = false
)

@JsonClass(generateAdapter = true)
data class ObjectBible(
    val id: String = "VEHICLE_V01",
    val name: String = "Primary Vehicle",
    val stateA: String = "",
    val transformationSteps: List<String> = emptyList(),
    val stateB: String = "",
    val physicalConstraints: List<String> = emptyList(),
    val isLocked: Boolean = false
)

@JsonClass(generateAdapter = true)
data class EnvironmentBible(
    val id: String = "ENV_E01",
    val location: String = "",
    val terrain: String = "",
    val architecture: String = "",
    val vegetation: String = "",
    val weather: String = "",
    val timeOfDay: String = "",
    val lightingDirection: String = "",
    val atmosphere: String = "",
    val backgroundLandmarks: List<String> = emptyList(),
    val continuityRules: List<String> = emptyList(),
    val isLocked: Boolean = false
)

@JsonClass(generateAdapter = true)
data class ReferenceFrame(
    val id: String,
    val title: String,
    val description: String,
    val frameType: String, // CHARACTER, OPENING, VEHICLE_STATE_A, TRANSFORMATION_INTERMEDIATE, VEHICLE_STATE_B, ENVIRONMENT, PAYOFF
    val prompt: String,
    val isLocked: Boolean = false,
    val imageUrl: String? = null,
    val isGenerated: Boolean = true
)
