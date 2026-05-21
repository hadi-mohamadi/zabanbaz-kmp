package ir.startup.zabanbaz.common.placement.domain

class SubmitPlacementTestUseCase(
    private val repository: PlacementRepository,
) {
    suspend operator fun invoke(
        sessionId: Int,
        answers: List<PlacementAnswer>,
    ): PlacementResult = repository.submitTest(sessionId, answers)
}
