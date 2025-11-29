package com.example.topplaygroundcompose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topplaygroundcompose.domain.model.Match
import com.example.topplaygroundcompose.domain.usecase.GetMatchByIdUseCase
import com.example.topplaygroundcompose.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchDetailUiState(
    val match: Match? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val getMatchByIdUseCase: GetMatchByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MatchDetailUiState(isLoading = true))
    val uiState: StateFlow<MatchDetailUiState> = _uiState.asStateFlow()
    
    fun loadMatch(matchId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val match = getMatchByIdUseCase(matchId)
                _uiState.value = _uiState.value.copy(
                    match = match,
                    isLoading = false,
                    error = if (match == null) "Матч не найден" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
    
    fun toggleFavorite() {
        val match = _uiState.value.match ?: return
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(match.matchId)
                val updatedMatch = getMatchByIdUseCase(match.matchId)
                _uiState.value = _uiState.value.copy(match = updatedMatch)
            } catch (e: Exception) {
            }
        }
    }
}

