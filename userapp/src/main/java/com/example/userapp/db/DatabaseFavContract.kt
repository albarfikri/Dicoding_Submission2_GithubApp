package com.example.userapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseFavContract {
    const val AUTHORITY = "com.example.github2"
    const val SCHEME = "content"

    internal class UserFavColumns : BaseColumns {
        companion object {
            private const val TABLE_NAME = "user_fav"
            const val _ID = "id"
            const val AVATAR = "avatar"
            const val USERNAME = "username"

            val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}