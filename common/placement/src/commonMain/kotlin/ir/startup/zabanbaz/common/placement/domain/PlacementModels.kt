package ir.startup.zabanbaz.common.placement.domain

data class PlacementOption(
    val id: Int,
    val text: String,
    val order: Int,
)

data class PlacementQuestion(
    val itemId: Int,
    val questionId: Int,
    val level: Int,
    val order: Int,
    val prompt: String,
    val options: List<PlacementOption>,
)

data class PlacementSession(
    val sessionId: Int,
    val questions: List<PlacementQuestion>,
)

data class PlacementAnswer(
    val itemId: Int,
    val optionId: Int,
)

data class PlacementResult(
    val sessionId: Int,
    val cefrLevel: String,
    val scoreCorrect: Int,
    val highestConsecutiveLevel: Int,
)
