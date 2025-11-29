package com.example.topplaygroundcompose.domain.usecase

import com.example.topplaygroundcompose.data.repository.MatchRepository
import com.example.topplaygroundcompose.domain.model.Match
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMatchesUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    operator fun invoke(): Flow<List<Match>> {
        return repository.getFavoriteMatches()
    }
}

