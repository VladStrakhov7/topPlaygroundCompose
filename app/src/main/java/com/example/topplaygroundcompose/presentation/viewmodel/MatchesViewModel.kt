package com.example.topplaygroundcompose.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topplaygroundcompose.domain.model.Match
import com.example.topplaygroundcompose.domain.usecase.GetMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchesUiState(
    val matches: List<Match> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MatchesUiState(isLoading = true))
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()
    
    init {
        loadMatches()
    }
    
    fun loadMatches() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                getMatchesUseCase()
                    .catch { e ->
                        Log.e("MatchesViewModel", "Error in flow", e)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error"
                        )
                    }
                    .collect { matches ->
                        Log.d("MatchesViewModel", "Received ${matches.size} matches")
                        _uiState.value = _uiState.value.copy(
                            matches = matches,
                            isLoading = false,
                            error = null
                        )
                    }
            } catch (e: Exception) {
                Log.e("MatchesViewModel", "Error loading matches", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}

