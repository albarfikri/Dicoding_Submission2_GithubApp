package com.example.github2.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_fav")
data class UserFavorite(
    @PrimaryKey
    val id: Int,
    val avatar: String,
    val username: String
) : Parcelable
