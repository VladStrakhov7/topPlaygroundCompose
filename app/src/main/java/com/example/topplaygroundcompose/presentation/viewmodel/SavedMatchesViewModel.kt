package com.example.topplaygroundcompose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topplaygroundcompose.domain.model.Match
import com.example.topplaygroundcompose.domain.usecase.GetFavoriteMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavedMatchesUiState(
    val matches: List<Match> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SavedMatchesViewModel @Inject constructor(
    private val getFavoriteMatchesUseCase: GetFavoriteMatchesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SavedMatchesUiState(isLoading = true))
    val uiState: StateFlow<SavedMatchesUiState> = _uiState.asStateFlow()
    
    init {
        loadMatches()
    }
    
    fun loadMatches() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getFavoriteMatchesUseCase()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
                .collect { matches ->
                    _uiState.value = _uiState.value.copy(
                        matches = matches,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
}

