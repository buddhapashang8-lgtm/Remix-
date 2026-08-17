package com.example.ai

import com.example.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class StoryboardGenerationResult(
    val storyboard: Storyboard,
    val characterBible: CharacterBible,
    val objectBible: ObjectBible,
    val environmentBible: EnvironmentBible,
    val referenceFrames: List<ReferenceFrame>
)

object StoryboardGenerator {

    suspend fun generateCompleteStoryboard(
        concept: RemixConcept,
        viralDNA: ViralDNA
    ): StoryboardGenerationResult = withContext(Dispatchers.Default) {
        val isSnow = concept.title.contains("Alpine", ignoreCase = true) || concept.fullConcept.contains("snow", ignoreCase = true) || concept.fullConcept.contains("glacier", ignoreCase = true)
        val isDesert = concept.title.contains("Dune", ignoreCase = true) || concept.fullConcept.contains("desert", ignoreCase = true)
        val isGlider = concept.title.contains("Skyline", ignoreCase = true) || concept.fullConcept.contains("glider", ignoreCase = true) || concept.fullConcept.contains("courier", ignoreCase = true)

        val characterBible = if (isSnow) {
            CharacterBible(
                id = "CHARACTER_C01",
                role = "Arctic Expedition Test Pilot",
                ageRange = "Late 20s to early 30s",
                appearance = "Athletic build, razor-sharp focus, technical Arctic survival armor",
                face = "Weathered high-altitude features, strong jawline, intense blue eyes behind tinted polarized visor",
                hair = "Dark cropped crew cut tucked inside thermal balaclava",
                facialHair = "Clean shaven / light tactical stubble",
                bodyType = "Toned, agile, aerodynamic posture",
                wardrobe = "Matte white ballistic thermal jumpsuit with fluorescent rescue-orange shoulder panels and reflective carbon-fiber knee braces",
                footwear = "Insulated high-traction mountaineering boots with integrated magnetic stirrups",
                accessories = "Wrist-mounted biometric altimeter, heated tactical gloves, emergency oxygen intake collar",
                continuityRules = listOf(
                    "White-and-orange jumpsuit pattern must remain identical with zero wardrobe changes",
                    "Helmet visor remains down with subtle internal HUD cyan glow",
                    "No logos, text, or extraneous patches on armor"
                )
            )
        } else if (isDesert) {
            CharacterBible(
                id = "CHARACTER_C01",
                role = "Dune Nomad Pathfinder",
                ageRange = "Mid 30s",
                appearance = "Weather-beaten desert traveler with sand-worn gear",
                face = "Sun-bronzed skin, keen observant gaze behind brass-rimmed sand goggles",
                hair = "Dark wind-swept hair wrapped in indigo desert keffiyeh",
                facialHair = "Trimmed beard",
                bodyType = "Lean and wiry",
                wardrobe = "Desert indigo linen wraps over distressed leather riding vest",
                footwear = "Soft leather desert riding boots with brass spurs",
                accessories = "Brass sand filtration mask, leather arm guards, compass bracer",
                continuityRules = listOf(
                    "Indigo linen wrap drape and brass goggles remain locked across all scenes",
                    "No magical costume switches between shots"
                )
            )
        } else {
            CharacterBible(
                id = "CHARACTER_C01",
                role = "Cyberpunk Data Courier",
                ageRange = "Mid 20s",
                appearance = "Sleek techwear specialist with high-agility exoskeleton rigging",
                face = "Sharp features with cyan-illuminated optical eye visor",
                hair = "Asymmetrical undercut hairstyle",
                facialHair = "None",
                bodyType = "Athletic and acrobatic",
                wardrobe = "Matte charcoal waterproof ballistic jacket, tactical cargo pants, reinforced spine brace",
                footwear = "High-impact parkour sneakers with glowing cyan tread",
                accessories = "Retinal HUD display, carbon fiber gauntlets, backpack flight harness",
                continuityRules = listOf(
                    "Cyan visor glow and matte charcoal jacket remain constant in all lighting",
                    "Backpack harness attachment points strictly fixed"
                )
            )
        }

        val objectBible = if (isSnow) {
            ObjectBible(
                id = "VEHICLE_V01",
                name = "Apex Metamorph Snow-Bike",
                stateA = "Aggressive off-road enduro motorcycle with knobby high-traction all-terrain rubber tires, lightweight exposed titanium trellis frame in matte gunmetal, high-clearance front forks, and raised orange exhaust.",
                transformationSteps = listOf(
                    "1. Front forks compress and rotate 30 degrees upward; front wheel separates down centerline into two halves.",
                    "2. Dual articulated 1.2m carbon-fiber carbide-edge steering skis swing outward and lock beneath the split wheel hubs with audible titanium latch pins.",
                    "3. Rear swingarm extends downward 25cm via pressurized hydraulic rams, deploying a 35cm-wide continuous reinforced rubber snow track with carbide ice studs.",
                    "4. Aerodynamic thermal fairing panels unfold from chassis flanks, channeling warm engine exhaust directly along the ski runners to prevent ice buildup.",
                    "5. Electronic suspension stiffens and engages active gyroscopic pitch stabilization in locked snow-vehicle mode."
                ),
                stateB = "High-speed tracked snow-bike (monotrack snowmobile) with dual front steering skis, continuous rear studded rubber track, lowered center of gravity, and aerodynamic snow deflectors.",
                physicalConstraints = listOf(
                    "All final snow-bike components derive physically from the motorcycle chassis (split wheels form ski mounts, swingarm holds track).",
                    "Strictly NO random magical morphing, disappearing parts, or teleporting components.",
                    "Color scheme remains matte gunmetal and emergency orange."
                )
            )
        } else if (isDesert) {
            ObjectBible(
                id = "VEHICLE_V01",
                name = "Dune Skimmer Repulsor Bike",
                stateA = "Heavy desert scrambler motorcycle with balloon sand tires, bronze-painted fuel tank, and exposed air filters.",
                transformationSteps = listOf(
                    "1. Wheels fold horizontally 90 degrees flush into wheel wells.",
                    "2. Dual ducted ceramic fan repulsors drop down beneath frame.",
                    "3. Side stabilizer strakes extend 45 degrees outward for ground-effect lift."
                ),
                stateB = "Low-altitude ground-effect hovercraft skimming 0.5m above sand dunes.",
                physicalConstraints = listOf(
                    "Bronze fuel tank and frame geometry preserved across both states."
                )
            )
        } else {
            ObjectBible(
                id = "VEHICLE_V01",
                name = "Aero-Courier Jet Rig",
                stateA = "Compact carbon-fiber aerodynamic backpack flight harness with folded wing struts.",
                transformationSteps = listOf(
                    "1. Pneumatic locks release; dual telescopic carbon-fiber wing spars extend 1.5m on each side.",
                    "2. Articulated flight feathers fan outward locking rigid aerodynamic airfoil surface.",
                    "3. Twin micro vector-thrust turbines swivel downward 45 degrees and ignite with cyan plasma glow."
                ),
                stateB = "Full 3-meter rigid wingspan powered jet-glider flight exoskeleton with twin vector turbines.",
                physicalConstraints = listOf(
                    "Rigid mechanical linkage deployment; no liquid morphing or sudden size scaling."
                )
            )
        }

        val environmentBible = if (isSnow) {
            EnvironmentBible(
                id = "ENV_E01",
                location = "Himalayan Glacial Amphitheater (Mt. Karakoram Ridge)",
                terrain = "Jagged black granite knife-edge ridge transitioning into sheer vertical blue-ice glacier wall and bottomless soft powder snowdrifts",
                architecture = "Raw untouched glacial wilderness; no human structures or powerlines",
                vegetation = "Sparse alpine lichen on bare rock; pristine crystalline snowpack",
                weather = "Sub-zero -18°C, crisp mountain air, high-altitude wind gusting 40 km/h, sparkling diamond snow dust suspended in atmosphere",
                timeOfDay = "Late Afternoon Golden Hour transitioning into Alpine Glow Twilight",
                lightingDirection = "Low 15-degree sun from western camera-left, casting long dramatic blue shadows on snow and blazing golden highlights on ice ridges",
                atmosphere = "Hyper-clear, thin high-altitude air with shimmering atmospheric refraction and crystalline ice glitter",
                backgroundLandmarks = listOf(
                    "Pyramidal 8000m serrated peak dominating north-west horizon",
                    "Deep glacial crevasse with deep cerulean blue internal ice glow",
                    "Curving snow cornice along ridge crest"
                ),
                continuityRules = listOf(
                    "Sun stays anchored at low western horizon throughout all 5 shots",
                    "Snow texture shows razor-sharp crystalline detail, never blurry or generic white"
                )
            )
        } else if (isDesert) {
            EnvironmentBible(
                id = "ENV_E01",
                location = "The Great Sinking Erg (Rub' al Khali Basin)",
                terrain = "Red sandstone canyon floor ending abruptly at massive shifting sand dunes with razor slipfaces",
                architecture = "Ancient weathered sandstone arches",
                vegetation = "Dry desert brush",
                weather = "Dry heat, gentle sand eddies swirling in wind",
                timeOfDay = "Dusk sunset with glowing amber horizon",
                lightingDirection = "Warm horizontal sunset light from west",
                atmosphere = "Golden atmospheric haze and heat shimmer",
                backgroundLandmarks = listOf("Towering 200m star dune", "Distant sandstone monoliths"),
                continuityRules = listOf("Dusk sunset horizon color consistent")
            )
        } else {
            EnvironmentBible(
                id = "ENV_E01",
                location = "Neo-Shinjuku High-Altitude Rooftop Canyon",
                terrain = "Urban skyscraper rooftops, antenna arrays, 500m vertical skyscraper chasm",
                architecture = "Mega-corporate glass-and-steel towers with holographic advertising lattices",
                vegetation = "None",
                weather = "Light misty drizzle with neon reflections",
                timeOfDay = "Night with vibrant neon cityscape",
                lightingDirection = "Omnidirectional neon blues, purples, and magenta holographic spill",
                atmosphere = "Cinematic cyberpunk volumetric rain haze",
                backgroundLandmarks = listOf("Megatower spire with red aviation beacon", "Massive 3D cyber whale hologram"),
                continuityRules = listOf("City skyline layout consistent across angles")
            )
        }

        val shots = if (isSnow) {
            listOf(
                StoryboardShot(
                    shotNumber = 1,
                    duration = 3.1,
                    storyPurpose = "High-energy setup: establish technical pilot, aggressive motorcycle, and extreme alpine speed.",
                    visualDescription = "Tracking profile shot of CHARACTER_C01 riding VEHICLE_V01 (State A motorcycle) at high speed along a razor-sharp granite ridge in ENV_E01, kicking up gravel and light snow spray.",
                    character = "CHARACTER_C01",
                    action = "Steers with aggressive forward lean along the narrow rocky crest, throttling up to 55 km/h.",
                    objectBehavior = "VEHICLE_V01 in motorcycle State A; tires grip rocky terrain, engine exhaust emits clean heat distortion.",
                    environment = "ENV_E01",
                    cameraFraming = "Medium side profile tracking shot",
                    cameraAngle = "Low angle 15 degrees",
                    cameraMovement = "Parallel horizontal dolly tracking at matched vehicle velocity (55 km/h)",
                    subjectMovement = "Constant left-to-right velocity along ridge",
                    lighting = "Low western golden hour sun casting long blue shadows on snow, rim-lighting helmet and shoulders",
                    transition = "Fast cut on granite boulder foreground wipe",
                    audio = "Throaty 4-stroke engine revs, studded rubber crunching rock, whistling high-altitude wind",
                    continuityRequirements = listOf(
                        "CHARACTER_C01 in white-and-orange jumpsuit with visor down",
                        "VEHICLE_V01 in matte gunmetal State A motorcycle config",
                        "ENV_E01 granite ridge with mountain peaks in background"
                    ),
                    generationPrompt = compilePrompt(
                        subject = "Arctic test pilot CHARACTER_C01 riding enduro motorcycle VEHICLE_V01 State A",
                        charId = "CHARACTER_C01 (white thermal ballistic jumpsuit, orange armor panels, tinted visor)",
                        envId = "ENV_E01 (Himalayan granite ridge, deep snowbanks, jagged peaks)",
                        action = "Riding at high speed along narrow rocky ridge line, leaning into curves",
                        objBehavior = "Motorcycle wheels spinning, knobby tires biting rock and powder snow",
                        camera = "Medium profile tracking shot, matched 55km/h speed, low angle 15 degrees",
                        lighting = "Cinematic golden hour sunset with crisp rim lighting and sharp snow glitter",
                        style = "8K Photorealistic IMAX Cinema, 35mm anamorphic lens, high shutter speed",
                        physics = "Grounded vehicle suspension dynamics, realistic tire snow spray rooster tail",
                        negatives = listOf("no CGI blur", "no cartoonish morphing", "no extra wheels", "no changing rider wardrobe", "no sunny tropical trees")
                    ),
                    negativeConstraints = listOf("no CGI blur", "no cartoonish morphing", "no extra wheels", "no changing rider wardrobe")
                ),
                StoryboardShot(
                    shotNumber = 2,
                    duration = 2.1,
                    storyPurpose = "The obstacle tension: rocky ridge abruptly ends into a massive 4-meter soft powder snowdrift and sheer glacier wall.",
                    visualDescription = "Low wide ground-level shot as the trail terminates into deep impassable snow. CHARACTER_C01 approaches at full speed without braking, heading straight for the powder bank.",
                    character = "CHARACTER_C01",
                    action = "Crouches tighter over handlebars, holding wide-open throttle directly toward the impassable powder snow wall.",
                    objectBehavior = "Front tire of VEHICLE_V01 makes first contact with deep powder snow, sending up a crystalline burst.",
                    environment = "ENV_E01",
                    cameraFraming = "Low wide shot from snow level looking up at oncoming motorcycle",
                    cameraAngle = "Low angle 30 degrees from deep snow surface",
                    cameraMovement = "Static camera with slight wind shake as bike rushes toward lens",
                    subjectMovement = "Charging forward directly toward camera lens, entering powder snow",
                    lighting = "Direct frontal sunlight highlighting snow crystals swirling in air",
                    transition = "Snap zoom cut to mechanical chassis detail",
                    audio = "Engine under heavy load, deep snow thud, pneumatic priming hiss and electronic alert chime",
                    continuityRequirements = listOf(
                        "Identical motorcycle chassis and rider suit",
                        "Snow wall thickness 4m with clear blue ice underneath"
                    ),
                    generationPrompt = compilePrompt(
                        subject = "CHARACTER_C01 riding VEHICLE_V01 directly into massive powder snowbank obstacle",
                        charId = "CHARACTER_C01 (helmet visor down, determined racing posture)",
                        envId = "ENV_E01 (trail termination into bottomless powder snow and vertical glacier face)",
                        action = "Motorcycle charges with zero braking into deep snow; front tire strikes powder boundary",
                        objBehavior = "Front wheel begins to displace snow; hydraulic actuators begin unlocking chassis links",
                        camera = "Low wide angle from snow surface looking up at motorcycle charging forward",
                        lighting = "High contrast alpine glare with sparkling diamond dust in foreground",
                        style = "High-speed 120fps slow-motion capture, extreme optical clarity",
                        physics = "Realistic fluid powder snow displacement, mechanical pre-deployment strain",
                        negatives = listOf("no magical teleporting", "no disappearing motorcycle", "no rider falling off")
                    ),
                    negativeConstraints = listOf("no magical teleporting", "no disappearing motorcycle", "no rider falling off")
                ),
                StoryboardShot(
                    shotNumber = 3,
                    duration = 4.2,
                    storyPurpose = "The viral core transformation: step-by-step mechanical metamorphosis from motorcycle to tracked snow-bike.",
                    visualDescription = "Tight 360-degree rotational detail shot capturing the intricate mechanical transformation of VEHICLE_V01: front wheel splits into dual carbide skis, rear track deploys, and heated side fairings lock.",
                    character = "CHARACTER_C01",
                    action = "Shifts center of gravity rearward onto footpegs as the chassis elevates and re-aligns.",
                    objectBehavior = "VEHICLE_V01 executes transformation: front wheels split and lock into skis, rear swingarm extends continuous track, titanium latches snap.",
                    environment = "ENV_E01",
                    cameraFraming = "Close-up dynamic orbiting shot rotating 180 degrees around vehicle mid-section",
                    cameraAngle = "Eye level to mechanical chassis and ski hinges",
                    cameraMovement = "Smooth rotational crane orbit tracing moving mechanical components",
                    subjectMovement = "Brief deceleration during mechanical lock, followed by instant surge as track engages",
                    lighting = "Bright directional sun glinting off machined titanium hinges, orange painted brackets, and carbon fiber weaves",
                    transition = "Speed ramp whip into wide glacier tracking",
                    audio = "Pneumatic cylinder hiss, mechanical latch clicks, electric actuator whine, sudden high-torque track roar",
                    continuityRequirements = listOf(
                        "Every component derives logically from motorcycle State A (split wheels to skis, swingarm to track)",
                        "No sudden morphing or disappearing geometry"
                    ),
                    generationPrompt = compilePrompt(
                        subject = "Intricate step-by-step mechanical transformation of VEHICLE_V01 from motorcycle to snow-bike",
                        charId = "CHARACTER_C01 (standing on footpegs, hands steady on heated grips)",
                        envId = "ENV_E01 (surrounded by swirling powder snow plumes)",
                        action = "Front wheel splits and locks into dual carbon skis; rear suspension extends continuous studded snow-track",
                        objBehavior = "Pneumatic pistons fire, mechanical latches engage with audible precision, heated side fairings deploy",
                        camera = "Close-up 180-degree orbiting camera tracking moving linkages and locking pins",
                        lighting = "Sun glints off brushed titanium and carbon weave; orange chassis highlights stand out against snow",
                        style = "Industrial mechanical precision cinema, hyper-detailed hard-surface rendering",
                        physics = "Rigid body kinematics, hydraulic line flex, real mechanical clearances, zero CGI morphing",
                        negatives = listOf("no soft liquid morphing", "no disappearing parts", "no floating components", "no random extra limbs")
                    ),
                    negativeConstraints = listOf("no soft liquid morphing", "no disappearing parts", "no floating components")
                ),
                StoryboardShot(
                    shotNumber = 4,
                    duration = 2.4,
                    storyPurpose = "Demonstration & proof of function: newly formed snow-bike carves effortlessly up the near-vertical glacier.",
                    visualDescription = "High-speed parallel chase shot of CHARACTER_C01 on VEHICLE_V01 (State B snow-bike) tearing up a 50-degree glacier face at 85 km/h, throwing a massive 10-meter crystalline snow rooster tail.",
                    character = "CHARACTER_C01",
                    action = "Counter-steers through deep powder, carving a razor-sharp S-turn across the frozen slope.",
                    objectBehavior = "Dual skis carve clean trenches; rear continuous track bites into packed ice, propelling machine with explosive acceleration.",
                    environment = "ENV_E01",
                    cameraFraming = "Low side-chase shot parallel to vehicle at 85 km/h",
                    cameraAngle = "Low angle 15 degrees tracking parallel across glacier face",
                    cameraMovement = "High-speed drone / cable-cam chase matched to 85 km/h vehicle velocity",
                    subjectMovement = "Blistering high-speed uphill traversal carving across slope",
                    lighting = "Warm golden hour backlit snow spray creating glowing halo and iridescent prism flares",
                    transition = "Smooth panning whip follow",
                    audio = "Screaming high-rev continuous track churn, razor ski slicing ice, triumphant orchestral electronic drop",
                    continuityRequirements = listOf(
                        "VEHICLE_V01 in fully locked State B snow-bike configuration",
                        "Consistent lighting direction and snow rooster tail physics"
                    ),
                    generationPrompt = compilePrompt(
                        subject = "CHARACTER_C01 riding VEHICLE_V01 State B snow-bike blasting up sheer glacier slope",
                        charId = "CHARACTER_C01 (leaning hard into turn, carving high-speed line)",
                        envId = "ENV_E01 (towering glacier face, deep pristine powder snow, blue ice crevasses)",
                        action = "Carving aggressive S-turn at 85 km/h up steep mountain slope",
                        objBehavior = "Continuous rear track throws massive 10m snow plume; dual front skis carve sharp tracks",
                        camera = "Low tracking chase shot parallel to snow-bike at matched 85 km/h speed",
                        lighting = "Backlit golden hour sunlight creating glowing translucent snow rooster tail",
                        style = "Extreme action sports cinematography, Red Bull cinema grade, pristine 8K",
                        physics = "Realistic centrifugal snow spray, ski edge bite, suspension compression under G-forces",
                        negatives = listOf("no wheels visible", "no slow movement", "no blurry snow", "no cartoonish effects")
                    ),
                    negativeConstraints = listOf("no wheels visible", "no slow movement", "no blurry snow")
                ),
                StoryboardShot(
                    shotNumber = 5,
                    duration = 3.2,
                    storyPurpose = "Final viral payoff: rider summits the highest peak, executes an epic air crest, and pauses in triumphant sunset silhouette.",
                    visualDescription = "Extreme wide cinematic shot: CHARACTER_C01 launches over the summit cornice into a moment of weightless air against the sunset horizon, lands smoothly on the plateau, and pauses with a raised fist salute.",
                    character = "CHARACTER_C01",
                    action = "Launches over cornice, lands in soft powder, turns snow-bike sideways, and raises gloved hand in victory salute.",
                    objectBehavior = "Suspension absorbs summit landing smoothly; snow settles around idling machine.",
                    environment = "ENV_E01",
                    cameraFraming = "Extreme wide majestic landscape master shot",
                    cameraAngle = "Slightly low angle looking up at mountain summit crest against sunset sky",
                    cameraMovement = "Slow majestic pull-back crane movement revealing the immense Himalayan mountain range",
                    subjectMovement = "Launch → Landing → Gentle drift stop → Static heroic silhouette",
                    lighting = "Breathtaking twilight alpine glow: fiery orange horizon transitioning into deep violet sky",
                    transition = "Slow cinematic fade to black",
                    audio = "Track engine settles into smooth idle hum, wind resonance across summit, final musical chord resolution",
                    continuityRequirements = listOf(
                        "Identical character and vehicle State B model",
                        "Summit peak matches background landmark established in Shot 1"
                    ),
                    generationPrompt = compilePrompt(
                        subject = "Heroic summit landing of CHARACTER_C01 on VEHICLE_V01 State B snow-bike at sunset",
                        charId = "CHARACTER_C01 (standing beside idling snow-bike, raising hand in victory)",
                        envId = "ENV_E01 (highest mountain summit ridge overlooking endless sea of clouds and sunset)",
                        action = "Snow-bike lands over crest, carves to a stop on snowy plateau against horizon",
                        objBehavior = "Engine idling, exhaust steam rising in sub-zero air, snow dust sparkling",
                        camera = "Extreme wide cinematic aerial crane pull-back revealing vast mountain amphitheater",
                        lighting = "Dramatic purple and gold twilight alpine glow on snow peaks",
                        style = "Epic masterpiece cinema ending frame, 8K ultra-wide anamorphic",
                        physics = "Soft powder dispersion on landing, atmospheric snow sparkle",
                        negatives = listOf("no low resolution", "no generic backdrop", "no changing vehicle design")
                    ),
                    negativeConstraints = listOf("no low resolution", "no generic backdrop", "no changing vehicle design")
                )
            )
        } else {
            // General / Cyberpunk Storyboard
            listOf(
                StoryboardShot(
                    shotNumber = 1,
                    duration = 3.1,
                    storyPurpose = "Rooftop sprint setup",
                    visualDescription = "CHARACTER_C01 sprints across high-rise rooftop in ENV_E01 carrying VEHICLE_V01 rig.",
                    character = "CHARACTER_C01",
                    action = "Vaults over ventilation pipes toward parapet at 24 km/h.",
                    objectBehavior = "Backpack rig hums with cyan LED readiness indicators.",
                    environment = "ENV_E01",
                    cameraFraming = "Medium side tracking",
                    cameraAngle = "Low angle 15 degrees",
                    cameraMovement = "Horizontal track matched to runner",
                    subjectMovement = "Sprint left to right",
                    lighting = "Neon blue and violet rim lighting",
                    transition = "Cut on ledge approach",
                    audio = "Footfalls, wind rush, pulsing synth beat",
                    continuityRequirements = listOf("Char suit and backpack rig locked"),
                    generationPrompt = "Cyberpunk courier CHARACTER_C01 sprinting across neon rooftop in ENV_E01 toward ledge.",
                    negativeConstraints = listOf("no cartoon style", "no blurry background")
                ),
                StoryboardShot(
                    shotNumber = 2,
                    duration = 2.1,
                    storyPurpose = "Leap into abyss",
                    visualDescription = "CHARACTER_C01 reaches the roof edge and leaps outward into 500m open sky.",
                    character = "CHARACTER_C01",
                    action = "Dives forward into zero-gravity freefall over city abyss.",
                    objectBehavior = "Pneumatic latches ignite on backpack rig.",
                    environment = "ENV_E01",
                    cameraFraming = "Wide vertical plunge view",
                    cameraAngle = "High steep down 60 degrees",
                    cameraMovement = "Boom down tracking fall",
                    subjectMovement = "Freefall acceleration",
                    lighting = "Dramatic skyline silhouette",
                    transition = "Snap zoom cut to backpack",
                    audio = "Wind whoosh, sudden silence, latch click",
                    continuityRequirements = listOf("Char in mid-air freefall posture"),
                    generationPrompt = "CHARACTER_C01 leaping into open skyscraper canyon in ENV_E01 with locked backpack rig.",
                    negativeConstraints = listOf("no safety net", "no parachute")
                ),
                StoryboardShot(
                    shotNumber = 3,
                    duration = 4.2,
                    storyPurpose = "Mid-air mechanical metamorphosis into jet glider",
                    visualDescription = "Close-up rotational shot as VEHICLE_V01 backpack snaps open into 3m rigid carbon wings with cyan micro-turbines.",
                    character = "CHARACTER_C01",
                    action = "Grips dual harness control handles, stabilizing flight posture.",
                    objectBehavior = "Telescopic spars extend, wings lock, turbines spool with cyan plasma.",
                    environment = "ENV_E01",
                    cameraFraming = "Close-up 180-degree orbit around transforming harness",
                    cameraAngle = "Eye level",
                    cameraMovement = "Smooth rotational crane orbit",
                    subjectMovement = "Arcing from vertical plunge to horizontal flight",
                    lighting = "Cyan turbine plasma glow illuminating underside",
                    transition = "Match cut on jet ignition",
                    audio = "Turbine spool whine, carbon latch lock, jet ignition roar",
                    continuityRequirements = listOf("Wing geometry derives from backpack specs"),
                    generationPrompt = "Mechanical transformation of VEHICLE_V01 harness into 3m carbon jet-wings in mid-air.",
                    negativeConstraints = listOf("no magical morphing", "no disappearing parts")
                ),
                StoryboardShot(
                    shotNumber = 4,
                    duration = 2.4,
                    storyPurpose = "High-speed skyscraper canyon banking",
                    visualDescription = "CHARACTER_C01 on VEHICLE_V01 banks at 45 degrees between glass skyscrapers at 160 km/h.",
                    character = "CHARACTER_C01",
                    action = "Banks smoothly around skyscraper spire leaving cyan vapor trails.",
                    objectBehavior = "Twin turbines fire steady cyan exhaust.",
                    environment = "ENV_E01",
                    cameraFraming = "Chase camera parallel at 160 km/h",
                    cameraAngle = "Low angle 10 degrees",
                    cameraMovement = "High-speed follow drone tracking",
                    subjectMovement = "High-speed carve through urban canyon",
                    lighting = "Holographic reflections bouncing off wet glass towers",
                    transition = "Smooth panning follow",
                    audio = "Jet roar, wind whistle, uplifting synth swell",
                    continuityRequirements = listOf("Glider wings rigid with twin exhaust trails"),
                    generationPrompt = "CHARACTER_C01 flying on VEHICLE_V01 jet wings banking through neon skyscraper canyon.",
                    negativeConstraints = listOf("no erratic motion", "no wing wobble")
                ),
                StoryboardShot(
                    shotNumber = 5,
                    duration = 3.2,
                    storyPurpose = "Soaring into sunset horizon payoff",
                    visualDescription = "Extreme wide aerial master: CHARACTER_C01 soars into the blazing golden twilight horizon above the clouds.",
                    character = "CHARACTER_C01",
                    action = "Soars straight toward the setting sun above the cloud line.",
                    objectBehavior = "Vapor trails expand across sky.",
                    environment = "ENV_E01",
                    cameraFraming = "Extreme wide master shot",
                    cameraAngle = "Elevated 15 degrees",
                    cameraMovement = "Slow majestic pull-back crane",
                    subjectMovement = "Cruising toward horizon vanishing point",
                    lighting = "Golden amber and purple sunset gradient",
                    transition = "Fade to black",
                    audio = "Triumphant electronic orchestral resolution",
                    continuityRequirements = listOf("Horizon sunset color matches overall palette"),
                    generationPrompt = "Extreme wide cinematic shot of CHARACTER_C01 soaring into sunset above cyberpunk clouds.",
                    negativeConstraints = listOf("no low resolution", "no choppy animation")
                )
            )
        }

        val referenceFrames = listOf(
            ReferenceFrame(
                id = "REF_01_CHAR",
                title = "Character Reference (${characterBible.id})",
                description = "Locked full-body design sheet for ${characterBible.role} in signature ${characterBible.wardrobe}.",
                frameType = "CHARACTER",
                prompt = "Character reference sheet of ${characterBible.role} (${characterBible.id}): ${characterBible.appearance}, ${characterBible.wardrobe}, ${characterBible.face}, high-definition studio turn-around with neutral lighting.",
                isLocked = true,
                isGenerated = true
            ),
            ReferenceFrame(
                id = "REF_02_OPENING",
                title = "Opening Scene Concept",
                description = "Master cinematic environment and initial vehicle cruising setup in ${environmentBible.location}.",
                frameType = "OPENING",
                prompt = "Cinematic establishing frame in ${environmentBible.id}: ${environmentBible.terrain}, ${environmentBible.lightingDirection}, ${characterBible.id} riding ${objectBible.id} State A.",
                isLocked = true,
                isGenerated = true
            ),
            ReferenceFrame(
                id = "REF_03_VEHICLE_STATE_A",
                title = "Vehicle State A (${objectBible.id})",
                description = "Base unmodified vehicle: ${objectBible.stateA}",
                frameType = "VEHICLE_STATE_A",
                prompt = "Hero 3/4 beauty pass of ${objectBible.id} in State A: ${objectBible.stateA}, studio hard-surface render with brushed titanium and orange accents.",
                isLocked = true,
                isGenerated = true
            ),
            ReferenceFrame(
                id = "REF_04_TRANSFORM_MID",
                title = "Transformation Intermediate State",
                description = "Mid-metamorphosis: mechanical linkages unlocking and extending.",
                frameType = "TRANSFORMATION_INTERMEDIATE",
                prompt = "High-detail cinematic close-up of ${objectBible.id} mid-transformation: ${objectBible.transformationSteps.joinToString("; ")}, titanium pistons and carbon linkages mid-deployment.",
                isLocked = true,
                isGenerated = true
            ),
            ReferenceFrame(
                id = "REF_05_VEHICLE_STATE_B",
                title = "Vehicle State B (${objectBible.id})",
                description = "Final transformed configuration: ${objectBible.stateB}",
                frameType = "VEHICLE_STATE_B",
                prompt = "Hero beauty pass of ${objectBible.id} in fully locked State B: ${objectBible.stateB}, high-torque continuous track and dual carbide steering skis.",
                isLocked = true,
                isGenerated = true
            ),
            ReferenceFrame(
                id = "REF_06_ENV",
                title = "Environment Master (${environmentBible.id})",
                description = "Comprehensive panoramic landscape palette for ${environmentBible.location}.",
                frameType = "ENVIRONMENT",
                prompt = "Panoramic master environment concept of ${environmentBible.id}: ${environmentBible.terrain}, ${environmentBible.weather}, ${environmentBible.lightingDirection}, 8K IMAX matte painting.",
                isLocked = true,
                isGenerated = true
            ),
            ReferenceFrame(
                id = "REF_07_PAYOFF",
                title = "Final Payoff & Horizon Exit",
                description = "Triumphant climax frame showing high-speed mastery at sunset.",
                frameType = "PAYOFF",
                prompt = "Epic cinema payoff frame of ${characterBible.id} on ${objectBible.id} State B summiting mountain peak against glorious twilight alpine glow.",
                isLocked = true,
                isGenerated = true
            )
        )

        val storyboard = Storyboard(
            title = concept.title,
            totalDuration = shots.sumOf { it.duration },
            shots = shots
        )

        StoryboardGenerationResult(
            storyboard = storyboard,
            characterBible = characterBible,
            objectBible = objectBible,
            environmentBible = environmentBible,
            referenceFrames = referenceFrames
        )
    }

    private fun compilePrompt(
        subject: String,
        charId: String,
        envId: String,
        action: String,
        objBehavior: String,
        camera: String,
        lighting: String,
        style: String,
        physics: String,
        negatives: List<String>
    ): String {
        return buildString {
            append("[SUBJECT]: $subject. ")
            append("[CHARACTER CONTINUITY]: $charId. ")
            append("[ENVIRONMENT]: $envId. ")
            append("[ACTION]: $action. ")
            append("[OBJECT BEHAVIOR]: $objBehavior. ")
            append("[CAMERA & COMPOSITION]: $camera. ")
            append("[LIGHTING & ATMOSPHERE]: $lighting. ")
            append("[STYLE & SENSOR]: $style. ")
            append("[PHYSICS & CAUSALITY]: $physics. ")
            append("[NEGATIVE CONSTRAINTS]: ${negatives.joinToString(", ")}.")
        }
    }
}
