package com.spacedlearning.app.domain.repository

import androidx.paging.PagingData
import com.spacedlearning.app.domain.model.Repetition
import com.spacedlearning.app.domain.model.RepetitionOrder
import com.spacedlearning.app.domain.model.RepetitionStatus
import com.spacedlearning.app.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface RepetitionRepository {
    fun getAllRepetitions(): Flow<PagingData<Repetition>>

    fun getDueRepetitions(
        userId: String,
        reviewDate: LocalDate? = null,
        status: RepetitionStatus? = null
    ): Flow<PagingData<Repetition>>

    suspend fun getRepetitionById(id: String): Resource<Repetition>

    suspend fun getRepetitionsByProgressId(progressId: String): Resource<List<Repetition>>

    suspend fun getRepetitionByProgressIdAndOrder(
        progressId: String,
        order: RepetitionOrder
    ): Resource<Repetition>

    suspend fun createRepetition(
        moduleProgressId: String,
        repetitionOrder: RepetitionOrder,
        status: RepetitionStatus? = null,
        reviewDate: LocalDate? = null
    ): Resource<Repetition>

    suspend fun createDefaultSchedule(progressId: String): Resource<List<Repetition>>

    suspend fun updateRepetition(
        id: String,
        status: RepetitionStatus?,
        reviewDate: LocalDate?
    ): Resource<Repetition>

    suspend fun deleteRepetition(id: String): Resource<Boolean>
}