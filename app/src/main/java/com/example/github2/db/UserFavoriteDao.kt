package com.example.github2.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserFavoriteDao {

    @Insert
    suspend fun addIntoFavorite(favorite: UserFavorite)

    @Query("SELECT * FROM user_fav")
    fun getUserFav(): LiveData<List<UserFavorite>>

    @Query("SELECT * FROM user_fav")
    fun getUserCpAll(): Cursor

    @Query("SELECT count(*) FROM user_fav WHERE user_fav.id = :id_user")
    suspend fun checkUserFav(id_user: Int): Int

    @Query("DELETE FROM user_fav WHERE user_fav.id = :id_user")
    suspend fun deletingFav(id_user: Int): Int

    @Query("SELECT * FROM user_fav ORDER BY username ASC")
    fun getfavWidget(): List<UserFavorite>
}