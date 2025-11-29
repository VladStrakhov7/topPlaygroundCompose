package com.example.topplaygroundcompose.domain.usecase

import com.example.topplaygroundcompose.data.repository.MatchRepository
import com.example.topplaygroundcompose.domain.model.Match
import javax.inject.Inject

class GetMatchByIdUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(matchId: Int): Match? {
        return repository.getMatchById(matchId)
    }
}

