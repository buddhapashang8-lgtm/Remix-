package com.example.data.repository

import com.example.data.local.JsonSerializer
import com.example.data.local.ProjectDao
import com.example.data.models.ProjectEntity
import com.example.data.models.ProjectStage
import com.example.domain.models.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ProjectRepository(private val projectDao: ProjectDao) {

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    suspend fun getProject(id: String): ProjectEntity? = projectDao.getProjectById(id)

    fun observeProject(id: String): Flow<ProjectEntity?> = projectDao.observeProjectById(id)

    suspend fun createNewProject(name: String, referenceVideo: ReferenceVideo? = null): ProjectEntity {
        val newProject = ProjectEntity(
            id = UUID.randomUUID().toString(),
            name = name.ifBlank { "Untitled Remix Project" },
            stage = if (referenceVideo != null) ProjectStage.UPLOAD else ProjectStage.UPLOAD,
            referenceVideoJson = referenceVideo?.let { JsonSerializer.toJson(it) },
            durationSeconds = referenceVideo?.durationSeconds ?: 15.0,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        projectDao.insertProject(newProject)
        return newProject
    }

    suspend fun saveReferenceAnalysis(
        projectId: String,
        video: ReferenceVideo,
        analysis: ReferenceAnalysis,
        viralDNA: ViralDNA
    ) {
        val current = projectDao.getProjectById(projectId) ?: return
        val updated = current.copy(
            stage = ProjectStage.ANALYZED,
            referenceVideoJson = JsonSerializer.toJson(video),
            referenceAnalysisJson = JsonSerializer.toJson(analysis),
            viralDnaJson = JsonSerializer.toJson(viralDNA),
            durationSeconds = analysis.totalDuration,
            shotCount = analysis.shots.size,
            updatedAt = System.currentTimeMillis()
        )
        projectDao.updateProject(updated)
    }

    suspend fun saveRemixConcept(
        projectId: String,
        settings: RemixSettings,
        concept: RemixConcept
    ) {
        val current = projectDao.getProjectById(projectId) ?: return
        val updated = current.copy(
            stage = ProjectStage.COMPARISON,
            remixSettingsJson = JsonSerializer.toJson(settings),
            remixConceptJson = JsonSerializer.toJson(concept),
            updatedAt = System.currentTimeMillis()
        )
        projectDao.updateProject(updated)
    }

    suspend fun saveStoryboardAndBibles(
        projectId: String,
        storyboard: Storyboard,
        charBible: CharacterBible,
        objBible: ObjectBible,
        envBible: EnvironmentBible,
        referenceFrames: List<ReferenceFrame>
    ) {
        val current = projectDao.getProjectById(projectId) ?: return
        val updated = current.copy(
            stage = ProjectStage.STORYBOARD,
            storyboardJson = JsonSerializer.toJson(storyboard),
            characterBibleJson = JsonSerializer.toJson(charBible),
            objectBibleJson = JsonSerializer.toJson(objBible),
            environmentBibleJson = JsonSerializer.toJson(envBible),
            referenceFramesJson = JsonSerializer.referenceFramesToJson(referenceFrames),
            durationSeconds = storyboard.totalDuration,
            shotCount = storyboard.shots.size,
            updatedAt = System.currentTimeMillis()
        )
        projectDao.updateProject(updated)
    }

    suspend fun updateStoryboard(projectId: String, storyboard: Storyboard) {
        val current = projectDao.getProjectById(projectId) ?: return
        val updated = current.copy(
            storyboardJson = JsonSerializer.toJson(storyboard),
            shotCount = storyboard.shots.size,
            updatedAt = System.currentTimeMillis()
        )
        projectDao.updateProject(updated)
    }

    suspend fun saveQualityReport(projectId: String, report: QualityReport) {
        val current = projectDao.getProjectById(projectId) ?: return
        val updated = current.copy(
            qualityReportJson = JsonSerializer.toJson(report),
            stage = ProjectStage.COMPLETED,
            updatedAt = System.currentTimeMillis()
        )
        projectDao.updateProject(updated)
    }

    suspend fun updateProject(project: ProjectEntity) {
        projectDao.updateProject(project.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteProject(id: String) {
        projectDao.deleteProjectById(id)
    }

    suspend fun duplicateProject(id: String): ProjectEntity? {
        val current = projectDao.getProjectById(id) ?: return null
        val duplicated = current.copy(
            id = UUID.randomUUID().toString(),
            name = "${current.name} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        projectDao.insertProject(duplicated)
        return duplicated
    }
}
