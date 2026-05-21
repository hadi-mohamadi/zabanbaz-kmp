package ir.startup.zabanbaz.common.placement.data.dto

import ir.startup.zabanbaz.common.placement.domain.PlacementAnswer
import ir.startup.zabanbaz.common.placement.domain.PlacementOption
import ir.startup.zabanbaz.common.placement.domain.PlacementQuestion
import ir.startup.zabanbaz.common.placement.domain.PlacementResult
import ir.startup.zabanbaz.common.placement.domain.PlacementSession
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacementOptionDto(
    val id: Int,
    val text: String,
    val order: Int,
) {
    fun toDomain(): PlacementOption = PlacementOption(id = id, text = text, order = order)
}

@Serializable
data class PlacementQuestionDto(
    @SerialName("item_id") val itemId: Int,
    @SerialName("question_id") val questionId: Int,
    val level: Int,
    val order: Int,
    val prompt: String,
    val options: List<PlacementOptionDto>,
) {
    fun toDomain(): PlacementQuestion = PlacementQuestion(
        itemId = itemId,
        questionId = questionId,
        level = level,
        order = order,
        prompt = prompt,
        options = options.map { it.toDomain() }.sortedBy { it.order },
    )
}

@Serializable
data class StartPlacementTestResponseDto(
    @SerialName("session_id") val sessionId: Int,
    val questions: List<PlacementQuestionDto>,
) {
    fun toDomain(): PlacementSession = PlacementSession(
        sessionId = sessionId,
        questions = questions.map { it.toDomain() }.sortedBy { it.order },
    )
}

@Serializable
data class PlacementAnswerDto(
    @SerialName("item_id") val itemId: Int,
    @SerialName("option_id") val optionId: Int,
)

@Serializable
data class SubmitPlacementTestRequestDto(
    val answers: List<PlacementAnswerDto>,
)

@Serializable
data class SubmitPlacementTestResponseDto(
    @SerialName("session_id") val sessionId: Int,
    @SerialName("cefr_level") val cefrLevel: String,
    @SerialName("score_correct") val scoreCorrect: Int,
    @SerialName("highest_consecutive_level") val highestConsecutiveLevel: Int,
) {
    fun toDomain(): PlacementResult = PlacementResult(
        sessionId = sessionId,
        cefrLevel = cefrLevel,
        scoreCorrect = scoreCorrect,
        highestConsecutiveLevel = highestConsecutiveLevel,
    )
}

fun PlacementAnswer.toDto(): PlacementAnswerDto =
    PlacementAnswerDto(itemId = itemId, optionId = optionId)
