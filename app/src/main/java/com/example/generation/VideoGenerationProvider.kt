package com.example.generation

import com.example.domain.models.StoryboardShot
import kotlinx.coroutines.flow.Flow

sealed class GenerationEvent {
    data class Progress(val shotNumber: Int, val progress: Float, val statusText: String) : GenerationEvent()
    data class Completed(val shotNumber: Int, val videoUrl: String, val thumbnailUrl: String) : GenerationEvent()
    data class Failed(val shotNumber: Int, val error: String) : GenerationEvent()
}

interface VideoGenerationProvider {
    val name: String
    val isConfigured: Boolean

    suspend fun generateShot(
        shot: StoryboardShot,
        referenceFrameUrls: List<String> = emptyList()
    ): Flow<GenerationEvent>
}
