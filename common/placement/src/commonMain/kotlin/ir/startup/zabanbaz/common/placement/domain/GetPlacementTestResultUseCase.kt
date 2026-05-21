package ir.startup.zabanbaz.common.placement.domain

class GetPlacementTestResultUseCase(
    private val repository: PlacementRepository,
) {
    suspend operator fun invoke(sessionId: Int): PlacementResult =
        repository.getTestResult(sessionId)
}
