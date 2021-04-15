package com.example.github2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.github2.db.DatabaseUser
import com.example.github2.db.UserFavoriteDao

class UserGithubContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.github2"
        const val TABLE_NAME = "user_fav"
        const val ID_GITHUB_USER = 1
        val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, ID_GITHUB_USER)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    private lateinit var userFavDao: UserFavoriteDao

    override fun onCreate(): Boolean {
        // if not null
        userFavDao = context?.let { Contex ->
            DatabaseUser.getUserDatabase(Contex)?.userFavDao()
        }!!
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            ID_GITHUB_USER -> {
                cursor = userFavDao.getUserCpAll()
                if (context != null) {
                    cursor.setNotificationUri(context?.contentResolver, uri)
                }
            }
            else -> {
                cursor = null
            }
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}