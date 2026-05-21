package ir.startup.zabanbaz.common.placement.domain

class StartPlacementTestUseCase(
    private val repository: PlacementRepository,
) {
    suspend operator fun invoke(): PlacementSession = repository.startTest()
}
