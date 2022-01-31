package com.numq.androidgrpcimageboard.data.board.local

import androidx.room.*

@Dao
interface BoardDao {

    @Query("SELECT * FROM boards LIMIT (:limit) OFFSET (:offset)")
    suspend fun getBoards(offset: Long, limit: Long): List<BoardDataModel>

    @Query("SELECT * FROM boards WHERE id=(:id)")
    suspend fun getBoardById(id: String): BoardDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoard(board: BoardDataModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBoards(boards: List<BoardDataModel>): Array<Long>

    @Update
    suspend fun updateBoard(board: BoardDataModel)

    @Delete
    suspend fun deleteBoard(board: BoardDataModel): Int

}