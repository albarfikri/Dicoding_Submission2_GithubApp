package com.example.userapp.helper

import android.database.Cursor
import com.example.userapp.db.DatabaseFavContract
import com.example.userapp.model.User

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val userFavList = ArrayList<User>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseFavContract.UserFavColumns._ID))
                val avatar =
                    getString(getColumnIndexOrThrow(DatabaseFavContract.UserFavColumns.AVATAR))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseFavContract.UserFavColumns.USERNAME))
                userFavList.add(User(id, avatar, username))
            }
        }
        return userFavList
    }
}
