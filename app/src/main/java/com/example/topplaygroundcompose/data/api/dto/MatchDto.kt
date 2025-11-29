package com.example.topplaygroundcompose.data.api.dto

import com.example.topplaygroundcompose.domain.model.Match
import com.example.topplaygroundcompose.domain.model.MatchResult
import com.example.topplaygroundcompose.domain.model.Team
import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("matchID")
    val matchId: Int,
    @SerializedName("matchDateTime")
    val matchDateTime: String,
    @SerializedName("team1")
    val team1: TeamDto,
    @SerializedName("team2")
    val team2: TeamDto,
    @SerializedName("matchResults")
    val matchResults: List<MatchResultDto>?,
    @SerializedName("leagueName")
    val leagueName: String
) {
    fun toDomain(): Match {
        return Match(
            matchId = matchId,
            matchDateTime = matchDateTime,
            team1 = team1.toDomain(),
            team2 = team2.toDomain(),
            matchResults = matchResults?.map { it.toDomain() },
            league = leagueName
        )
    }
}

data class TeamDto(
    @SerializedName("teamId")
    val teamId: Int,
    @SerializedName("teamName")
    val teamName: String,
    @SerializedName("teamIconUrl")
    val teamIconUrl: String?
) {
    fun toDomain(): Team {
        return Team(
            teamId = teamId,
            teamName = teamName,
            teamIconUrl = teamIconUrl
        )
    }
}

data class MatchResultDto(
    @SerializedName("resultID")
    val resultId: Int,
    @SerializedName("resultName")
    val resultName: String,
    @SerializedName("pointsTeam1")
    val pointsTeam1: Int?,
    @SerializedName("pointsTeam2")
    val pointsTeam2: Int?
) {
    fun toDomain(): MatchResult {
        return MatchResult(
            resultId = resultId,
            resultName = resultName,
            pointsTeam1 = pointsTeam1,
            pointsTeam2 = pointsTeam2
        )
    }
}

