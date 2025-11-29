package com.example.topplaygroundcompose.data.repository

import android.util.Log
import com.example.topplaygroundcompose.data.api.OpenLigaApi
import com.example.topplaygroundcompose.data.local.MatchDao
import com.example.topplaygroundcompose.data.local.entity.MatchEntity
import com.example.topplaygroundcompose.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MatchRepository(
    private val api: OpenLigaApi,
    private val matchDao: MatchDao
) {
    suspend fun getMatches(): Flow<List<Match>> = flow {
        try {
            Log.d("MatchRepository", "Loading matches from API")
            val matchesDto = try {
                api.getLastMatches()
            } catch (e: Exception) {
                Log.e("MatchRepository", "API call failed", e)
                throw e
            }
            Log.d("MatchRepository", "Received ${matchesDto.size} matches from API")
            
            val matches = try {
                matchesDto.map { 
                    try {
                        it.toDomain()
                    } catch (e: Exception) {
                        Log.e("MatchRepository", "Error converting match to domain", e)
                        null
                    }
                }.filterNotNull()
            } catch (e: Exception) {
                Log.e("MatchRepository", "Error converting matches to domain", e)
                emptyList()
            }
            Log.d("MatchRepository", "Converted to domain models: ${matches.size}")
            
            if (matches.isNotEmpty()) {
                try {
                    val existingMatches = matchDao.getAllMatchesSync()
                    val favoriteMap = existingMatches.associateBy { it.matchId }
                    
                    val matchesToInsert = matches.map { match ->
                        val existing = favoriteMap[match.matchId]
                        MatchEntity.fromDomain(match, isFavorite = existing?.isFavorite ?: false)
                    }
                    
                    matchDao.deleteAllMatches()
                    matchDao.insertMatches(matchesToInsert)
                    Log.d("MatchRepository", "Saved ${matches.size} matches to database")
                } catch (e: Exception) {
                    Log.e("MatchRepository", "Error saving to database", e)
                }
            }
            
            emit(matches)
            Log.d("MatchRepository", "Emitted ${matches.size} matches")
        } catch (e: Exception) {
            Log.e("MatchRepository", "Error loading matches", e)
            e.printStackTrace()
            try {
                val cachedMatches = matchDao.getAllMatchesSync().map { it.toDomain() }
                Log.d("MatchRepository", "Loaded ${cachedMatches.size} cached matches")
                emit(cachedMatches)
            } catch (dbException: Exception) {
                Log.e("MatchRepository", "Error loading cached matches", dbException)
                dbException.printStackTrace()
                emit(emptyList())
            }
        }
    }
    
    fun getCachedMatches(): Flow<List<Match>> {
        return matchDao.getAllMatches().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    fun getFavoriteMatches(): Flow<List<Match>> {
        return matchDao.getFavoriteMatches().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun toggleFavorite(matchId: Int) {
        val match = matchDao.getMatchById(matchId)
        if (match != null) {
            matchDao.updateFavoriteStatus(matchId, !match.isFavorite)
        } else {
            val allMatches = matchDao.getAllMatchesSync()
            val foundMatch = allMatches.find { it.matchId == matchId }
            if (foundMatch != null) {
                matchDao.updateFavoriteStatus(matchId, !foundMatch.isFavorite)
            }
        }
    }
    
    suspend fun getMatchById(matchId: Int): Match? {
        var entity = matchDao.getMatchById(matchId)
        if (entity == null) {
            val allMatches = matchDao.getAllMatchesSync()
            entity = allMatches.find { it.matchId == matchId }
        }
        return entity?.toDomain()
    }
}

