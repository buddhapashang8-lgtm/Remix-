package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

enum class ProjectStage {
    UPLOAD,
    ANALYZED,
    REMIX_DRAFT,
    COMPARISON,
    STORYBOARD,
    GENERATING,
    COMPLETED
}

@Entity(tableName = "projects")
@JsonClass(generateAdapter = true)
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val stage: ProjectStage = ProjectStage.UPLOAD,
    val referenceVideoJson: String? = null,
    val referenceAnalysisJson: String? = null,
    val viralDnaJson: String? = null,
    val remixSettingsJson: String? = null,
    val remixConceptJson: String? = null,
    val storyboardJson: String? = null,
    val characterBibleJson: String? = null,
    val objectBibleJson: String? = null,
    val environmentBibleJson: String? = null,
    val referenceFramesJson: String? = null,
    val qualityReportJson: String? = null,
    val durationSeconds: Double = 15.0,
    val shotCount: Int = 0,
    val thumbnailUri: String? = null
)
