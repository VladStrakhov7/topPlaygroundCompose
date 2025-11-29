package com.example.topplaygroundcompose.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.topplaygroundcompose.R
import com.example.topplaygroundcompose.presentation.viewmodel.MatchDetailViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: Int,
    onBackClick: () -> Unit = {},
    viewModel: MatchDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(matchId) {
        viewModel.loadMatch(matchId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.match_details)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(uiState.error ?: stringResource(R.string.error_loading_matches))
                }
            }
            uiState.match != null -> {
                val match = uiState.match!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = match.league,
                                fontSize = 14.sp,
                                color = colorResource(R.color.text_secondary)
                            )
                            
                            Text(
                                text = formatDate(match.matchDateTime),
                                fontSize = 14.sp,
                                color = colorResource(R.color.text_secondary),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = match.team1.teamName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                val result = match.matchResults?.firstOrNull { it.resultName == "Endergebnis" }
                                if (result != null && result.pointsTeam1 != null && result.pointsTeam2 != null) {
                                    Text(
                                        text = "${result.pointsTeam1} : ${result.pointsTeam2}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.primary),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                } else {
                                    Text(
                                        text = "- : -",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.text_secondary),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                                
                                Text(
                                    text = match.team2.teamName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    
                    Button(
                        onClick = { viewModel.toggleFavorite() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = if (match.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = if (match.isFavorite) {
                                stringResource(R.string.remove_from_saved)
                            } else {
                                stringResource(R.string.save_match)
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun formatDate(dateTime: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        date?.let { outputFormat.format(it) } ?: dateTime
    } catch (e: Exception) {
        dateTime
    }
}

