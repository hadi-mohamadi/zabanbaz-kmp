package ir.startup.zabanbaz.common.placement.domain

interface PlacementRepository {
    suspend fun startTest(): PlacementSession

    suspend fun submitTest(sessionId: Int, answers: List<PlacementAnswer>): PlacementResult

    suspend fun getTestResult(sessionId: Int): PlacementResult
}
