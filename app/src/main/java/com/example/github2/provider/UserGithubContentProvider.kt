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
        const val authority = "com.example.github2"
        const val tableName = "user_fav"
        const val idGithubUser = 1
        val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        sUriMatcher.addURI(authority, tableName, idGithubUser)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    private lateinit var userFavDao: UserFavoriteDao

    override fun onCreate(): Boolean {
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
            idGithubUser -> {
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
    ): Int = 0
}