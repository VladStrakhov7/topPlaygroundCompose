package com.example.topplaygroundcompose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.topplaygroundcompose.domain.model.Match
import com.example.topplaygroundcompose.domain.model.MatchResult
import com.example.topplaygroundcompose.domain.model.Team

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey
    val matchId: Int,
    val matchDateTime: String,
    val team1Id: Int,
    val team1Name: String,
    val team1IconUrl: String?,
    val team2Id: Int,
    val team2Name: String,
    val team2IconUrl: String?,
    val pointsTeam1: Int?,
    val pointsTeam2: Int?,
    val league: String,
    val isFavorite: Boolean = false
) {
    fun toDomain(): Match {
        return Match(
            matchId = matchId,
            matchDateTime = matchDateTime,
            team1 = Team(
                teamId = team1Id,
                teamName = team1Name,
                teamIconUrl = team1IconUrl
            ),
            team2 = Team(
                teamId = team2Id,
                teamName = team2Name,
                teamIconUrl = team2IconUrl
            ),
            matchResults = if (pointsTeam1 != null && pointsTeam2 != null) {
                listOf(
                    MatchResult(
                        resultId = 0,
                        resultName = "Endergebnis",
                        pointsTeam1 = pointsTeam1,
                        pointsTeam2 = pointsTeam2
                    )
                )
            } else null,
            league = league,
            isFavorite = isFavorite
        )
    }
    
    companion object {
        fun fromDomain(match: Match, isFavorite: Boolean = false): MatchEntity {
            val finalResult = match.matchResults?.firstOrNull { it.resultName == "Endergebnis" }
            return MatchEntity(
                matchId = match.matchId,
                matchDateTime = match.matchDateTime,
                team1Id = match.team1.teamId,
                team1Name = match.team1.teamName,
                team1IconUrl = match.team1.teamIconUrl,
                team2Id = match.team2.teamId,
                team2Name = match.team2.teamName,
                team2IconUrl = match.team2.teamIconUrl,
                pointsTeam1 = finalResult?.pointsTeam1,
                pointsTeam2 = finalResult?.pointsTeam2,
                league = match.league,
                isFavorite = isFavorite
            )
        }
    }
}

