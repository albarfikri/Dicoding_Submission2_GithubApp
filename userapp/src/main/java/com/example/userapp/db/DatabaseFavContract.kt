package com.example.userapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseFavContract {
    const val authority = "com.example.github2"
    const val scheme = "content"

    internal class UserFavColumns : BaseColumns {
        companion object {
            private const val tableName = "user_fav"
            const val _id = "id"
            const val avatar = "avatar"
            const val username = "username"

            val CONTENT_URI = Uri.Builder().scheme(scheme)
                .authority(authority)
                .appendPath(tableName)
                .build()
        }
    }
}