package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.*
import com.example.data.local.AppDatabase
import com.example.data.local.JsonSerializer
import com.example.data.models.ProjectEntity
import com.example.data.models.ProjectStage
import com.example.domain.models.*
import com.example.generation.GenerationEvent
import com.example.generation.MockVideoProvider
import com.example.generation.VideoGenerationProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AnalysisProgressState(
    val isAnalyzing: Boolean = false,
    val stepText: String = "",
    val progressFraction: Float = 0f
)

data class RemixGenProgressState(
    val isGenerating: Boolean = false,
    val stepText: String = "",
    val progressFraction: Float = 0f
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = com.example.data.repository.ProjectRepository(
        AppDatabase.getDatabase(application).projectDao()
    )

    val allProjects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeProjectId = MutableStateFlow<String?>(null)
    val activeProjectId: StateFlow<String?> = _activeProjectId.asStateFlow()

    private val _activeProject = MutableStateFlow<ProjectEntity?>(null)
    val activeProject: StateFlow<ProjectEntity?> = _activeProject.asStateFlow()

    // Loaded Domain Objects for active project
    private val _currentReferenceVideo = MutableStateFlow<ReferenceVideo?>(null)
    val currentReferenceVideo: StateFlow<ReferenceVideo?> = _currentReferenceVideo.asStateFlow()

    private val _currentAnalysis = MutableStateFlow<ReferenceAnalysis?>(null)
    val currentAnalysis: StateFlow<ReferenceAnalysis?> = _currentAnalysis.asStateFlow()

    private val _currentViralDNA = MutableStateFlow<ViralDNA?>(null)
    val currentViralDNA: StateFlow<ViralDNA?> = _currentViralDNA.asStateFlow()

    private val _currentRemixSettings = MutableStateFlow(RemixSettings())
    val currentRemixSettings: StateFlow<RemixSettings> = _currentRemixSettings.asStateFlow()

    private val _currentRemixConcept = MutableStateFlow<RemixConcept?>(null)
    val currentRemixConcept: StateFlow<RemixConcept?> = _currentRemixConcept.asStateFlow()

    private val _currentStoryboard = MutableStateFlow<Storyboard?>(null)
    val currentStoryboard: StateFlow<Storyboard?> = _currentStoryboard.asStateFlow()

    private val _currentCharBible = MutableStateFlow<CharacterBible?>(null)
    val currentCharBible: StateFlow<CharacterBible?> = _currentCharBible.asStateFlow()

    private val _currentObjBible = MutableStateFlow<ObjectBible?>(null)
    val currentObjBible: StateFlow<ObjectBible?> = _currentObjBible.asStateFlow()

    private val _currentEnvBible = MutableStateFlow<EnvironmentBible?>(null)
    val currentEnvBible: StateFlow<EnvironmentBible?> = _currentEnvBible.asStateFlow()

    private val _currentReferenceFrames = MutableStateFlow<List<ReferenceFrame>>(emptyList())
    val currentReferenceFrames: StateFlow<List<ReferenceFrame>> = _currentReferenceFrames.asStateFlow()

    private val _currentQualityReport = MutableStateFlow<QualityReport?>(null)
    val currentQualityReport: StateFlow<QualityReport?> = _currentQualityReport.asStateFlow()

    // Progress States
    private val _analysisProgress = MutableStateFlow(AnalysisProgressState())
    val analysisProgress: StateFlow<AnalysisProgressState> = _analysisProgress.asStateFlow()

    private val _remixGenProgress = MutableStateFlow(RemixGenProgressState())
    val remixGenProgress: StateFlow<RemixGenProgressState> = _remixGenProgress.asStateFlow()

    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage: StateFlow<String?> = _notificationMessage.asStateFlow()

    // Generation Provider
    private var videoProvider: VideoGenerationProvider = MockVideoProvider()
    private val activeGenJobs = mutableMapOf<Int, Job>()

    init {
        // Observe active project updates from Room
        viewModelScope.launch {
            _activeProjectId.collect { id ->
                if (id != null) {
                    val project = repository.getProject(id)
                    _activeProject.value = project
                    loadDomainModels(project)
                } else {
                    _activeProject.value = null
                    clearDomainModels()
                }
            }
        }
    }

    private fun loadDomainModels(project: ProjectEntity?) {
        if (project == null) {
            clearDomainModels()
            return
        }
        _currentReferenceVideo.value = JsonSerializer.fromJson(project.referenceVideoJson)
        _currentAnalysis.value = JsonSerializer.fromJson(project.referenceAnalysisJson)
        _currentViralDNA.value = JsonSerializer.fromJson(project.viralDnaJson)
        _currentRemixSettings.value = JsonSerializer.fromJson(project.remixSettingsJson) ?: RemixSettings()
        _currentRemixConcept.value = JsonSerializer.fromJson(project.remixConceptJson)
        _currentStoryboard.value = JsonSerializer.fromJson(project.storyboardJson)
        _currentCharBible.value = JsonSerializer.fromJson(project.characterBibleJson)
        _currentObjBible.value = JsonSerializer.fromJson(project.objectBibleJson)
        _currentEnvBible.value = JsonSerializer.fromJson(project.environmentBibleJson)
        _currentReferenceFrames.value = JsonSerializer.referenceFramesFromJson(project.referenceFramesJson)
        _currentQualityReport.value = JsonSerializer.fromJson(project.qualityReportJson)
    }

    private fun clearDomainModels() {
        _currentReferenceVideo.value = null
        _currentAnalysis.value = null
        _currentViralDNA.value = null
        _currentRemixSettings.value = RemixSettings()
        _currentRemixConcept.value = null
        _currentStoryboard.value = null
        _currentCharBible.value = null
        _currentObjBible.value = null
        _currentEnvBible.value = null
        _currentReferenceFrames.value = emptyList()
        _currentQualityReport.value = null
    }

    fun selectProject(projectId: String) {
        _activeProjectId.value = projectId
    }

    fun createProject(name: String, presetVideo: ReferenceVideo? = null, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val video = presetVideo ?: PresetVideos.samples.first()
            val project = repository.createNewProject(name, video)
            _activeProjectId.value = project.id
            _currentReferenceVideo.value = video
            onCreated(project.id)
        }
    }

    fun setReferenceVideo(video: ReferenceVideo) {
        _currentReferenceVideo.value = video
        val pid = _activeProjectId.value ?: return
        viewModelScope.launch {
            val current = repository.getProject(pid) ?: return@launch
            val updated = current.copy(
                referenceVideoJson = JsonSerializer.toJson(video),
                durationSeconds = video.durationSeconds
            )
            repository.updateProject(updated)
            _activeProject.value = updated
        }
    }

    fun startReferenceAnalysis(onComplete: () -> Unit = {}) {
        val video = _currentReferenceVideo.value ?: return
        val pid = _activeProjectId.value ?: return

        viewModelScope.launch {
            _analysisProgress.value = AnalysisProgressState(isAnalyzing = true, stepText = "Initializing Multimodal Gemini Pipeline...", progressFraction = 0.1f)
            delay(500)

            _analysisProgress.value = AnalysisProgressState(isAnalyzing = true, stepText = "Detecting temporal shot boundaries & motion vectors...", progressFraction = 0.35f)
            delay(600)

            _analysisProgress.value = AnalysisProgressState(isAnalyzing = true, stepText = "Abstracting core reusable story mechanisms...", progressFraction = 0.65f)
            delay(700)

            _analysisProgress.value = AnalysisProgressState(isAnalyzing = true, stepText = "Extracting structured Viral DNA (Hook, Camera, Motion, Pacing)...", progressFraction = 0.9f)
            delay(500)

            val (analysis, viralDNA) = ViralDNAExtractor.analyzeReferenceVideo(video)
            _currentAnalysis.value = analysis
            _currentViralDNA.value = viralDNA

            repository.saveReferenceAnalysis(pid, video, analysis, viralDNA)
            _activeProject.value = repository.getProject(pid)

            _analysisProgress.value = AnalysisProgressState(isAnalyzing = false, stepText = "Analysis Complete!", progressFraction = 1.0f)
            notify("Reference Video Analyzed & Viral DNA Extracted!")
            onComplete()
        }
    }

    fun updateRemixPrompt(prompt: String) {
        _currentRemixSettings.value = _currentRemixSettings.value.copy(userPrompt = prompt)
    }

    fun setDimensionControl(dimensionName: String, mode: RemixControlMode) {
        val updated = _currentRemixSettings.value.controls.toMutableMap()
        updated[dimensionName] = mode
        _currentRemixSettings.value = _currentRemixSettings.value.copy(controls = updated)
    }

    fun applyPresetControls(mode: RemixControlMode) {
        val updated = _currentRemixSettings.value.controls.mapValues { mode }
        _currentRemixSettings.value = _currentRemixSettings.value.copy(controls = updated)
    }

    fun generateRemixConcept(onComplete: () -> Unit = {}) {
        val viralDNA = _currentViralDNA.value ?: return
        val settings = _currentRemixSettings.value
        val pid = _activeProjectId.value ?: return

        viewModelScope.launch {
            _remixGenProgress.value = RemixGenProgressState(isGenerating = true, stepText = "Reasoning functional element substitutions...", progressFraction = 0.2f)
            delay(600)

            _remixGenProgress.value = RemixGenProgressState(isGenerating = true, stepText = "Enforcing physical causality & mechanical feasibility...", progressFraction = 0.55f)
            delay(700)

            _remixGenProgress.value = RemixGenProgressState(isGenerating = true, stepText = "Compiling 6-stage remix concept & structural diffs...", progressFraction = 0.85f)
            delay(500)

            val concept = RemixReasoningEngine.generateRemixConcept(viralDNA, settings)
            _currentRemixConcept.value = concept

            repository.saveRemixConcept(pid, settings, concept)
            _activeProject.value = repository.getProject(pid)

            _remixGenProgress.value = RemixGenProgressState(isGenerating = false, stepText = "Concept Ready!", progressFraction = 1.0f)
            notify("New Remix Concept Synthesized!")
            onComplete()
        }
    }

    fun approveConceptAndGenerateStoryboard(onComplete: () -> Unit = {}) {
        val concept = _currentRemixConcept.value ?: return
        val viralDNA = _currentViralDNA.value ?: return
        val pid = _activeProjectId.value ?: return

        viewModelScope.launch {
            _remixGenProgress.value = RemixGenProgressState(isGenerating = true, stepText = "Synthesizing Character, Vehicle & Environment Bibles...", progressFraction = 0.3f)
            delay(600)

            _remixGenProgress.value = RemixGenProgressState(isGenerating = true, stepText = "Generating Reference Frame Plan & Production Prompts...", progressFraction = 0.7f)
            delay(600)

            val result = StoryboardGenerator.generateCompleteStoryboard(concept, viralDNA)

            _currentStoryboard.value = result.storyboard
            _currentCharBible.value = result.characterBible
            _currentObjBible.value = result.objectBible
            _currentEnvBible.value = result.environmentBible
            _currentReferenceFrames.value = result.referenceFrames

            repository.saveStoryboardAndBibles(
                pid,
                result.storyboard,
                result.characterBible,
                result.objectBible,
                result.environmentBible,
                result.referenceFrames
            )
            _activeProject.value = repository.getProject(pid)

            _remixGenProgress.value = RemixGenProgressState(isGenerating = false, stepText = "Storyboard Generated!", progressFraction = 1.0f)
            notify("Storyboard, Bibles & Reference Frames Generated!")
            onComplete()
        }
    }

    fun toggleCharacterLock() {
        val current = _currentCharBible.value ?: return
        val updated = current.copy(isLocked = !current.isLocked)
        _currentCharBible.value = updated
        persistBibles()
    }

    fun toggleObjectLock() {
        val current = _currentObjBible.value ?: return
        val updated = current.copy(isLocked = !current.isLocked)
        _currentObjBible.value = updated
        persistBibles()
    }

    fun toggleEnvironmentLock() {
        val current = _currentEnvBible.value ?: return
        val updated = current.copy(isLocked = !current.isLocked)
        _currentEnvBible.value = updated
        persistBibles()
    }

    fun toggleReferenceFrameLock(frameId: String) {
        val list = _currentReferenceFrames.value.map {
            if (it.id == frameId) it.copy(isLocked = !it.isLocked) else it
        }
        _currentReferenceFrames.value = list
        val pid = _activeProjectId.value ?: return
        viewModelScope.launch {
            val project = repository.getProject(pid) ?: return@launch
            val updated = project.copy(
                referenceFramesJson = JsonSerializer.referenceFramesToJson(list)
            )
            repository.updateProject(updated)
        }
    }

    private fun persistBibles() {
        val pid = _activeProjectId.value ?: return
        val c = _currentCharBible.value
        val o = _currentObjBible.value
        val e = _currentEnvBible.value
        viewModelScope.launch {
            val project = repository.getProject(pid) ?: return@launch
            val updated = project.copy(
                characterBibleJson = JsonSerializer.toJson(c),
                objectBibleJson = JsonSerializer.toJson(o),
                environmentBibleJson = JsonSerializer.toJson(e)
            )
            repository.updateProject(updated)
        }
    }

    fun updateShotPrompt(shotNumber: Int, newPrompt: String) {
        val sb = _currentStoryboard.value ?: return
        val updatedShots = sb.shots.map {
            if (it.shotNumber == shotNumber) it.copy(generationPrompt = newPrompt) else it
        }
        val updatedSb = sb.copy(shots = updatedShots)
        _currentStoryboard.value = updatedSb
        val pid = _activeProjectId.value ?: return
        viewModelScope.launch {
            repository.updateStoryboard(pid, updatedSb)
        }
    }

    fun generateShotVideo(shotNumber: Int) {
        val sb = _currentStoryboard.value ?: return
        val shot = sb.shots.find { it.shotNumber == shotNumber } ?: return
        val pid = _activeProjectId.value ?: return

        activeGenJobs[shotNumber]?.cancel()

        activeGenJobs[shotNumber] = viewModelScope.launch {
            // Set status to QUEUED / GENERATING
            updateShotStatus(shotNumber, ShotGenStatus.GENERATING, 0.05f)

            videoProvider.generateShot(shot).collect { event ->
                when (event) {
                    is GenerationEvent.Progress -> {
                        updateShotStatus(shotNumber, ShotGenStatus.GENERATING, event.progress)
                    }
                    is GenerationEvent.Completed -> {
                        val updatedShots = (_currentStoryboard.value?.shots ?: emptyList()).map {
                            if (it.shotNumber == shotNumber) {
                                it.copy(
                                    status = ShotGenStatus.COMPLETED,
                                    progress = 1.0f,
                                    videoPreviewUrl = event.videoUrl,
                                    thumbnailPreviewUrl = event.thumbnailUrl
                                )
                            } else it
                        }
                        val updatedSb = sb.copy(shots = updatedShots)
                        _currentStoryboard.value = updatedSb
                        repository.updateStoryboard(pid, updatedSb)
                        notify("Shot #$shotNumber Render Completed!")
                    }
                    is GenerationEvent.Failed -> {
                        updateShotStatus(shotNumber, ShotGenStatus.FAILED, 0f, event.error)
                        notify("Shot #$shotNumber Generation Failed: ${event.error}")
                    }
                }
            }
        }
    }

    fun generateAllShots() {
        val sb = _currentStoryboard.value ?: return
        sb.shots.forEach { shot ->
            if (shot.status != ShotGenStatus.COMPLETED) {
                generateShotVideo(shot.shotNumber)
            }
        }
    }

    private fun updateShotStatus(shotNumber: Int, status: ShotGenStatus, progress: Float, error: String? = null) {
        val sb = _currentStoryboard.value ?: return
        val updatedShots = sb.shots.map {
            if (it.shotNumber == shotNumber) it.copy(status = status, progress = progress, errorMessage = error) else it
        }
        val updatedSb = sb.copy(shots = updatedShots)
        _currentStoryboard.value = updatedSb
    }

    fun runQualityControlAudit(onComplete: () -> Unit = {}) {
        val sb = _currentStoryboard.value ?: return
        val pid = _activeProjectId.value ?: return

        viewModelScope.launch {
            val report = QualityControlAgent.runMultimodalQualityAudit(sb)
            _currentQualityReport.value = report
            repository.saveQualityReport(pid, report)
            _activeProject.value = repository.getProject(pid)
            notify("Quality Control Audit Completed: Overall Consistency ${report.overallContinuity}%")
            onComplete()
        }
    }

    fun applyAIFixToShot(shotNumber: Int) {
        val report = _currentQualityReport.value ?: return
        val suggestion = report.shotFixSuggestions[shotNumber.toString()] ?: "Refine mechanical kinematics and eliminate motion blur."
        val sb = _currentStoryboard.value ?: return
        val shot = sb.shots.find { it.shotNumber == shotNumber } ?: return

        val correctivePrompt = QualityControlAgent.generateCorrectivePrompt(shot.generationPrompt, suggestion)
        updateShotPrompt(shotNumber, correctivePrompt)
        notify("AI Fix Applied to Shot #$shotNumber! Re-generating shot...")
        generateShotVideo(shotNumber)
    }

    fun duplicateActiveProject(onDuplicated: (String) -> Unit) {
        val pid = _activeProjectId.value ?: return
        viewModelScope.launch {
            val dup = repository.duplicateProject(pid)
            if (dup != null) {
                _activeProjectId.value = dup.id
                notify("Project Duplicated!")
                onDuplicated(dup.id)
            }
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            if (_activeProjectId.value == projectId) {
                _activeProjectId.value = null
            }
            notify("Project Deleted.")
        }
    }

    fun notify(message: String) {
        _notificationMessage.value = message
    }

    fun clearNotification() {
        _notificationMessage.value = null
    }
}
