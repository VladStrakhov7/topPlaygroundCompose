package com.example.topplaygroundcompose.domain.model

data class Match(
    val matchId: Int,
    val matchDateTime: String,
    val team1: Team,
    val team2: Team,
    val matchResults: List<MatchResult>?,
    val league: String,
    val isFavorite: Boolean = false
)

data class Team(
    val teamId: Int,
    val teamName: String,
    val teamIconUrl: String?
)

data class MatchResult(
    val resultId: Int,
    val resultName: String,
    val pointsTeam1: Int?,
    val pointsTeam2: Int?
)

