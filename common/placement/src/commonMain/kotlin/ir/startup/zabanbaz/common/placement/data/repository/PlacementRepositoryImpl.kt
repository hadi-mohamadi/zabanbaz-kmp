package ir.startup.zabanbaz.common.placement.data.repository

import ir.startup.zabanbaz.common.placement.data.datasource.PlacementRemoteDataSource
import ir.startup.zabanbaz.common.placement.domain.PlacementAnswer
import ir.startup.zabanbaz.common.placement.domain.PlacementRepository
import ir.startup.zabanbaz.common.placement.domain.PlacementResult
import ir.startup.zabanbaz.common.placement.domain.PlacementSession

class PlacementRepositoryImpl(
    private val remoteDataSource: PlacementRemoteDataSource,
) : PlacementRepository {
    override suspend fun startTest(): PlacementSession =
        remoteDataSource.startTest()

    override suspend fun submitTest(
        sessionId: Int,
        answers: List<PlacementAnswer>,
    ): PlacementResult = remoteDataSource.submitTest(sessionId, answers)

    override suspend fun getTestResult(sessionId: Int): PlacementResult =
        remoteDataSource.getTestResult(sessionId)
}
