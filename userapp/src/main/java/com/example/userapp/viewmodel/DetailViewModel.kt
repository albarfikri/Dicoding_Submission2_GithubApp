package com.example.userapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userapp.db.DatabaseFavContract
import com.example.userapp.helper.MappingHelper
import com.example.userapp.model.User

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    // Start Favorite Part
    private val listFav = MutableLiveData<ArrayList<User>>()
    private val loadingStates = MutableLiveData<Boolean>()

    fun getLoadingState(): LiveData<Boolean> = loadingStates

    fun setUserFav(context: Context) {
        val cursor = context.contentResolver.query(
            DatabaseFavContract.UserFavColumns.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val convertedFavList = MappingHelper.mapCursorToArrayList(cursor)
        if (convertedFavList.count() > 0) {
            listFav.postValue(convertedFavList)
            loadingStates.postValue(true)
        }else{
            listFav.postValue(convertedFavList)
            loadingStates.postValue(false)
        }
    }
    fun getFavUser(): LiveData<ArrayList<User>> = listFav
}
