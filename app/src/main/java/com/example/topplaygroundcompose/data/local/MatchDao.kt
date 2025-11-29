package com.example.topplaygroundcompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.topplaygroundcompose.data.local.entity.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY matchDateTime DESC")
    fun getAllMatches(): Flow<List<MatchEntity>>
    
    @Query("SELECT * FROM matches ORDER BY matchDateTime DESC")
    suspend fun getAllMatchesSync(): List<MatchEntity>
    
    @Query("SELECT * FROM matches WHERE isFavorite = 1 ORDER BY matchDateTime DESC")
    fun getFavoriteMatches(): Flow<List<MatchEntity>>
    
    @Query("SELECT * FROM matches WHERE matchId = :matchId")
    suspend fun getMatchById(matchId: Int): MatchEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity)
    
    @Query("UPDATE matches SET isFavorite = :isFavorite WHERE matchId = :matchId")
    suspend fun updateFavoriteStatus(matchId: Int, isFavorite: Boolean)
    
    @Query("DELETE FROM matches")
    suspend fun deleteAllMatches()
}

