package com.example.topplaygroundcompose.domain.usecase

import com.example.topplaygroundcompose.data.repository.MatchRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(matchId: Int) {
        repository.toggleFavorite(matchId)
    }
}

