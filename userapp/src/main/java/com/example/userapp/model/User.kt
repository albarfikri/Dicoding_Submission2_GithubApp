package com.example.userapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var avatar: String,
    var username: String
) : Parcelable


