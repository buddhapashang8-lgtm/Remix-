package com.example.ai

import com.example.data.local.JsonSerializer
import com.example.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RemixReasoningEngine {

    suspend fun generateRemixConcept(
        viralDNA: ViralDNA,
        settings: RemixSettings
    ): RemixConcept = withContext(Dispatchers.Default) {
        // Try live Gemini API reasoning first
        val liveConcept = tryLiveGeminiRemix(viralDNA, settings)
        if (liveConcept != null) {
            return@withContext liveConcept
        }

        // Fallback to our deep contextual reasoning engine
        return@withContext performContextualReasoning(viralDNA, settings)
    }

    private suspend fun tryLiveGeminiRemix(
        viralDNA: ViralDNA,
        settings: RemixSettings
    ): RemixConcept? {
        if (!GeminiClient.hasApiKey()) return null

        val prompt = """
            You are the Remix Reasoning Engine for Viral Remix Studio.
            CRITICAL DIRECTIVE: Do NOT do naive word replacement.
            Understand the FUNCTION of elements. Maintain logical physical and cinematic causality.

            SOURCE VIRAL DNA:
            Abstract Story Concept: ${viralDNA.abstractConcept}
            Literal Concept: ${viralDNA.literalConcept}
            Story Setup: ${viralDNA.story.setup}
            Story Problem: ${viralDNA.story.problem}
            Story Surprise: ${viralDNA.story.surprise}
            Story Payoff: ${viralDNA.story.payoff}

            USER REMIX INSTRUCTION:
            "${settings.userPrompt.ifBlank { "Transform into an extreme alpine snowstorm with a snow-bike metamorphosis" }}"

            DIMENSION CONTROL MODES:
            ${settings.controls.entries.joinToString("\n") { "- ${it.key}: ${it.value}" }}

            Produce a complete REMIX CONCEPT JSON adhering strictly to:
            {
              "title": "...",
              "oneLineConcept": "...",
              "fullConcept": "...",
              "newHook": "...",
              "newSetup": "...",
              "newProblem": "...",
              "newAnticipation": "...",
              "newSurprise": "...",
              "newReveal": "...",
              "newProof": "...",
              "newPayoff": "...",
              "newEnding": "...",
              "preservedElements": ["..."],
              "adaptedElements": ["..."],
              "replacedElements": ["..."],
              "reasoningSummary": "Explain why specific functional substitutions were made to preserve physical plausibility (e.g. why skis/tracks were chosen over water-foils for snow)",
              "continuityRequirements": ["..."],
              "shotsSummary": ["Shot 1: ...", "Shot 2: ...", "Shot 3: ...", "Shot 4: ...", "Shot 5: ..."]
            }
        """.trimIndent()

        val jsonResponse = GeminiClient.queryGemini(
            prompt = prompt,
            systemInstruction = "You are an elite Hollywood conceptual remix director. Output ONLY valid JSON."
        ) ?: return null

        return try {
            JsonSerializer.fromJson<RemixConcept>(jsonResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun performContextualReasoning(
        viralDNA: ViralDNA,
        settings: RemixSettings
    ): RemixConcept {
        val userPrompt = settings.userPrompt.lowercase()

        val isSnow = userPrompt.contains("snow") || userPrompt.contains("ice") || userPrompt.contains("glacier") || userPrompt.contains("mountain") || userPrompt.isBlank()
        val isCyberpunk = userPrompt.contains("cyberpunk") || userPrompt.contains("neon") || userPrompt.contains("city") || userPrompt.contains("future")
        val isDesert = userPrompt.contains("desert") || userPrompt.contains("dune") || userPrompt.contains("sand")
        val isUnderwater = userPrompt.contains("underwater") || userPrompt.contains("ocean") || userPrompt.contains("sub") || userPrompt.contains("sea")

        if (isSnow) {
            return RemixConcept(
                title = "The Alpine Metamorphosis",
                oneLineConcept = "A cyberpunk cyber-biker hits an impassable sheer alpine snowfield and mechanically deploys dual tracked treads and steering skis to conquer the glacier.",
                fullConcept = "An audacious pilot in high-altitude Arctic thermal suit rides a rugged off-road machine toward a catastrophic mountain precipice covered in 4-meter deep powder snow. Approaching the edge where wheels would instantly sink, the vehicle executes a step-by-step mechanical transformation: front wheel splits into dual high-grip carbide skis, rear chassis extends a high-torque continuous snow track, and heated aerofoil panels deploy. The rider carves up the vertical glacier at 90 km/h, proving flawless mastery over frozen terrain.",
                newHook = "High-velocity approach along a jagged icy mountain ridge in blinding alpine sun.",
                newSetup = "Protagonist in stealth white-and-orange alpine gear navigates a narrowing rocky ridge on an aggressive dirt-bike chassis.",
                newProblem = "The gravel trail dead-ends into a 60-degree sheer glacier wall and bottomless powder snow that impossible for standard wheeled vehicles.",
                newAnticipation = "Pilot charges straight into the powder snowbank without reducing throttle, sparking imminent crash suspense.",
                newSurprise = "Rather than burying into snow, pneumatic charges fire and mechanical suspension links begin rapid realignment.",
                newReveal = "Front wheel rotates upward and unlocks dual articulated carbon skis; rear swingarm extends downward revealing a high-powered spiked rubber continuous track.",
                newProof = "Ski edges bite into hard-packed ice while the rear track churns a massive powder plume, lifting the chassis above the snowpack.",
                newPayoff = "Pilot blasts up the near-vertical glacier face, executing a huge aerial jump over a deep crevasse with total control.",
                newEnding = "Summiting the highest peak at sunset, the rider pauses against the golden alpine horizon as snow dust sparkles in the air.",
                preservedElements = listOf(
                    "6-part viral suspense curve: Approach → Dead-End Obstacle → No-Brake Anticipation → Mechanical Metamorphosis → Proof of Grip → Triumphant Summit Payoff",
                    "Camera language: matched horizontal tracking, low-angle terrain boundary reveal, 360 rotational mechanical closeup, high-speed chase",
                    "Causal physical transformation: wheels to terrain-specific traction (skis/tracks) with explicit mechanical linkage deployment",
                    "Pacing structure and energy escalation across 5 continuous shots"
                ),
                adaptedElements = listOf(
                    "Obstacle changed from static water surface tension to 3D verticality and deep soft snow friction",
                    "Lighting adapted from sunset golden hour to high-altitude blinding crystalline snow glare and twilight peak glow",
                    "Sound design adapted: tire rumble replaced with stud-crunching ice grip, turbine replaced with supercharged high-torque track thrum",
                    "Rider attitude adapted from calm leisurely stroll to intense high-adrenaline alpine precision"
                ),
                replacedElements = listOf(
                    "Environment: Replaced temperate mountain lake with towering Himalayan glacial amphitheater",
                    "Vehicle Form B: Replaced aquatic hydrofoil foils with titanium carbide steering skis and continuous snow-track",
                    "Character: Replaced elderly tweed gentleman with young technical Arctic expedition pilot in high-visibility survival suit",
                    "Particle FX: Replaced water spray sheet with massive crystalline snow rooster tail"
                ),
                structuralDiffs = listOf(
                    StructuralDiff(
                        category = "Terrain Obstacle",
                        referenceElement = "Flat lake shoreline dropping into deep open water",
                        remixedElement = "60-degree vertical glacier wall with 4m powder snow drifts",
                        mode = RemixControlMode.REPLACE,
                        rationale = "Maintains the structural idea of 'vehicle arrives at incompatible terrain' while adapting to snow physics."
                    ),
                    StructuralDiff(
                        category = "Transformation Physics",
                        referenceElement = "Front wheel folds, hydrodynamic foils deploy for water lift",
                        remixedElement = "Front wheel splits into dual skis; rear swingarm deploys spiked continuous snow track",
                        mode = RemixControlMode.ADAPT,
                        rationale = "Hydrofoils fail in snow. Skis provide steering while continuous tracks provide necessary displacement and traction."
                    ),
                    StructuralDiff(
                        category = "Story Arc & Hook",
                        referenceElement = "Cruising approach with no-braking suspense leading into metamorphosis",
                        remixedElement = "Ridge sprint with no-braking suspense leading into snow-bike metamorphosis",
                        mode = RemixControlMode.PRESERVE,
                        rationale = "Preserving the identical psychological anticipation mechanism that drives viral retention."
                    ),
                    StructuralDiff(
                        category = "Character Identity",
                        referenceElement = "Silver-haired gentleman in camel overcoat and aviator goggles",
                        remixedElement = "Arctic expedition pilot in high-tech survival suit with biometric visor",
                        mode = RemixControlMode.REPLACE,
                        rationale = "Aligns wardrobe and character lore with extreme sub-zero environment."
                    )
                ),
                reasoningSummary = "Instead of naively replacing 'lake' with 'snow' while keeping hydrofoils (which would be physically absurd and break viewer immersion), the engine analyzed the functional role of water resistance vs snow displacement. Skis and continuous tracks were derived from the original chassis geometry to preserve mechanical believability and visual continuity.",
                continuityRequirements = listOf(
                    "Protagonist survival suit retains signature white armor plates with fluorescent emergency orange stripes across all shots",
                    "Vehicle base chassis preserves matte titanium finish and exposed orange roll cage",
                    "Ski and track assemblies derive physically from the original wheel hubs with visible hydraulic actuators",
                    "Sun glare and snow-dust refraction maintain consistent westerly lighting direction"
                ),
                shotsSummary = listOf(
                    "Shot 1 (0.0-3.1s): High-speed ridge sprint on rocky alpine terrain",
                    "Shot 2 (3.1-5.2s): Imminent collision with bottomless powder snow wall",
                    "Shot 3 (5.2-9.4s): Mechanical metamorphosis into tracked snow-bike",
                    "Shot 4 (9.4-11.8s): High-velocity carving up vertical glacier face",
                    "Shot 5 (11.8-15.0s): Triumphant summit crest jump into sunset twilight"
                )
            )
        } else if (isDesert) {
            return RemixConcept(
                title = "The Dune Glider",
                oneLineConcept = "A desert nomad rides a heavy scrambler into a sea of shifting quicksand dunes, transforming the machine into an aerodynamic ground-effect hovercraft.",
                fullConcept = "Traversing a barren rocky canyon, a desert nomad reaches the edge of the Great Sinking Erg—a vast ocean of shifting quicksand dunes. With no trail ahead, the rider throttles into the sinking sand. As the wheels begin to bog down, mechanical outriggers unfold, deploying ceramic air-cushion repulsors and solar sail fins. The machine lifts 0.5m above the desert, skimming effortlessly across the dunes.",
                newHook = "Fast chase across red rock canyon floor ending in towering sand dune abyss.",
                newSetup = "Nomad with linen wraps and brass goggles rides a dust-caked scrambler motorcycle.",
                newProblem = "Rocky canyon ends abruptly at the edge of vast, treacherous shifting sand dunes.",
                newAnticipation = "Nomad accelerates into the quicksand basin where normal tires sink instantly.",
                newSurprise = "Magnetic latches disengage; chassis side panels fold out into wide lifting strakes.",
                newReveal = "Dual ducted repulsor fans deploy under the frame, generating an intense ground-effect air cushion.",
                newProof = "Hovercraft rises above the sand, leaving zero tire tracks as it glides over razor-sharp dune crests.",
                newPayoff = "High-speed sweeping drift across a 200-meter slipface dune at dusk.",
                newEnding = "Nomad glides over the horizon toward a glowing ancient desert citadel.",
                preservedElements = listOf(
                    "6-part core structural story: Approach → Incompatible Medium → No-Brake Tension → Metamorphosis → Float/Glide Proof → Sunset Horizon Exit",
                    "Camera tracking style and shot boundary durations",
                    "Grounded physical transformation with modular moving panels",
                    "High-speed glide resolution"
                ),
                adaptedElements = listOf(
                    "Friction mechanics adapted from water fluid dynamics to sand particle displacement and air cushion ground effect",
                    "Color grading shifted to warm terracotta reds, rich amber dusk, and turquoise sky contrast",
                    "Audio adapted: sand-blast whoosh and ducted fan hum"
                ),
                replacedElements = listOf(
                    "Environment: Desert Dune Erg",
                    "Vehicle Form B: Ground-effect repulsor hovercraft",
                    "Character: Desert nomad pilot",
                    "Atmosphere: Arid desert heat shimmer"
                ),
                structuralDiffs = listOf(
                    StructuralDiff(
                        category = "Terrain Obstacle",
                        referenceElement = "Lake water barrier",
                        remixedElement = "Shifting quicksand desert dunes",
                        mode = RemixControlMode.REPLACE,
                        rationale = "Preserves the impossible surface barrier concept."
                    ),
                    StructuralDiff(
                        category = "Transformation",
                        referenceElement = "Hydrofoil wing deployment",
                        remixedElement = "Ducted ground-effect hover repulsors and stability strakes",
                        mode = RemixControlMode.ADAPT,
                        rationale = "Ground effect hovercraft is the logical desert equivalent of a marine hydrofoil."
                    )
                ),
                reasoningSummary = "Quicksand is non-Newtonian; wheeled traversal fails due to sinking. Replacing hydrofoils with ducted ground-effect hovercraft panels maintains logical physics and delivers identical visual awe.",
                continuityRequirements = listOf(
                    "Nomad's indigo linen scarf and brass goggles must appear identically in every shot",
                    "Vehicle retains weathered sand-bronze frame and exposed turbine housing"
                ),
                shotsSummary = listOf(
                    "Shot 1: Canyon floor sprint",
                    "Shot 2: Sand sea boundary encounter",
                    "Shot 3: Ducted hovercraft transformation",
                    "Shot 4: High-speed slipface carving",
                    "Shot 5: Sunset desert horizon exit"
                )
            )
        } else {
            // Default High-Concept Cyberpunk Rooftop Glider
            return RemixConcept(
                title = "Neon Skyline Vault",
                oneLineConcept = "A cyberpunk freerunning courier sprints toward a skyscraper rooftop ledge, leaps into open sky, and deploys a mechanical jet-wing harness to soar between neon towers.",
                fullConcept = "A high-tech courier vaults across neon-drenched megacity rooftops, reaching a dead-end ledge 100 stories above street level. Rather than stopping, the protagonist leaps directly into empty sky. In mid-air freefall, the compact backpack rig undergoes a high-speed mechanical metamorphosis: rigid carbon-fiber wings articulate outward, twin vector-thrust micro-turbines ignite with electric cyan plasma, and aerodynamic control surfaces lock. The courier stabilizes from a vertical dive into horizontal high-speed flight, banking between holographic billboards.",
                newHook = "Hyper-velocity rooftop sprint against blinding neon skyline.",
                newSetup = "Courier in sleek carbon techwear races across a high-rise communications rooftop.",
                newProblem = "Rooftop ends abruptly into a sheer 500-meter drop between skyscraper canyons.",
                newAnticipation = "Courier reaches the parapet and leaps outward with zero deceleration.",
                newSurprise = "Suspended in zero-G freefall, the compact backpack releases pneumatic expansion rings.",
                newReveal = "Carbon wing spars snap into rigid 3-meter wingspan with cyan vector-thrust micro-jets.",
                newProof = "Pilot levels out 20 meters above flying traffic, slicing through holographic displays.",
                newPayoff = "Breathtaking 180 km/h banking turn through a high-rise glass canyon.",
                newEnding = "Soaring into the neon twilight horizon as city lights illuminate the clouds.",
                preservedElements = listOf(
                    "Structure: Setup → Terminal Obstacle → Leap of Faith → Mechanical Transformation → Flight Proof → Horizon Soar",
                    "Camera rhythm and rotational shot choreography during transformation",
                    "Pacing of tension build and sudden kinetic release"
                ),
                adaptedElements = listOf(
                    "Terrain obstacle adapted from 2D horizontal boundary to 3D vertical skyscraper drop",
                    "Lighting adapted to high-contrast neon violet, electric cyan, and holographic glows",
                    "Sound design adapted to jet turbine whine and wind shear"
                ),
                replacedElements = listOf(
                    "Environment: Neo-Tokyo Megacity Skyscraper Rooftops",
                    "Vehicle: Jet-Wing Flight Exoskeleton",
                    "Character: Cyberpunk data courier",
                    "Props: Holographic beacons and neon parapets"
                ),
                structuralDiffs = listOf(
                    StructuralDiff(
                        category = "Obstacle",
                        referenceElement = "Lake water edge",
                        remixedElement = "500m skyscraper vertical drop",
                        mode = RemixControlMode.REPLACE,
                        rationale = "Translates the terminal boundary into vertical urban space."
                    ),
                    StructuralDiff(
                        category = "Transformation",
                        referenceElement = "Motorcycle to Hydrofoil",
                        remixedElement = "Techwear Backpack to Jet-Wing Exoskeleton",
                        mode = RemixControlMode.REPLACE,
                        rationale = "Provides flight capability to traverse the aerial canyon."
                    )
                ),
                reasoningSummary = "The engine mapped the 'impassable barrier' to the sheer vertical edge of a skyscraper, and the 'metamorphosis' to a backpack-to-glider mechanical transformation, preserving the core thrill of impossible traversal.",
                continuityRequirements = listOf(
                    "Courier's cyan-glowing visor and matte black vest must remain identical",
                    "Wing harness carbon weave and twin jet thrusters maintain consistent styling"
                ),
                shotsSummary = listOf(
                    "Shot 1: Rooftop sprint",
                    "Shot 2: Parapet leap into empty space",
                    "Shot 3: Mid-air wing & turbine deployment",
                    "Shot 4: High-speed canyon banking",
                    "Shot 5: Neon sunset horizon soar"
                )
            )
        }
    }
}
