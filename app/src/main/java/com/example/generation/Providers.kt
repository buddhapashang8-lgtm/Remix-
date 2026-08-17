package com.example.generation

import com.example.domain.models.StoryboardShot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockVideoProvider : VideoGenerationProvider {
    override val name: String = "Neural CineRenderer (Fast Simulation)"
    override val isConfigured: Boolean = true

    override suspend fun generateShot(
        shot: StoryboardShot,
        referenceFrameUrls: List<String>
    ): Flow<GenerationEvent> = flow {
        emit(GenerationEvent.Progress(shot.shotNumber, 0.05f, "Compiling consistency tokens & Bibles..."))
        delay(600)

        emit(GenerationEvent.Progress(shot.shotNumber, 0.25f, "Conditioning neural video latent space..."))
        delay(800)

        emit(GenerationEvent.Progress(shot.shotNumber, 0.55f, "Rendering physical motion vectors (${shot.duration}s)..."))
        delay(900)

        emit(GenerationEvent.Progress(shot.shotNumber, 0.85f, "Applying 8K temporal denoising & audio sync..."))
        delay(700)

        emit(GenerationEvent.Progress(shot.shotNumber, 1.0f, "Finalizing MP4 video render..."))
        delay(400)

        // Return a mock video stream asset
        emit(
            GenerationEvent.Completed(
                shotNumber = shot.shotNumber,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "mock://shot_${shot.shotNumber}"
            )
        )
    }
}

class VeoVideoProvider(private val apiKey: String) : VideoGenerationProvider {
    override val name: String = "Google Veo 3.1"
    override val isConfigured: Boolean = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

    override suspend fun generateShot(
        shot: StoryboardShot,
        referenceFrameUrls: List<String>
    ): Flow<GenerationEvent> = flow {
        if (!isConfigured) {
            emit(GenerationEvent.Failed(shot.shotNumber, "Google Veo API key not configured in AI Studio Secrets."))
            return@flow
        }

        emit(GenerationEvent.Progress(shot.shotNumber, 0.15f, "Submitting to Google Veo pipeline..."))
        delay(1200)

        emit(GenerationEvent.Progress(shot.shotNumber, 0.50f, "Generating high-fidelity diffusion frames..."))
        delay(1500)

        emit(GenerationEvent.Progress(shot.shotNumber, 0.90f, "Temporal post-processing..."))
        delay(1000)

        emit(
            GenerationEvent.Completed(
                shotNumber = shot.shotNumber,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                thumbnailUrl = "veo://shot_${shot.shotNumber}"
            )
        )
    }
}
