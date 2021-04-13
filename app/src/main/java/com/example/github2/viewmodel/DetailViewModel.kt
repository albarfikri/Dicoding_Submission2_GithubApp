package com.example.github2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.github2.db.DatabaseUser
import com.example.github2.db.UserFavorite
import com.example.github2.db.UserFavoriteDao
import com.example.github2.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val detailUser = MutableLiveData<User>()

    // Start Favorite Part
    private var userFavDao: UserFavoriteDao? = null
    private var userDb: DatabaseUser? = null

    init {
        userDb = DatabaseUser.getUserDatabase(application)
        userFavDao = userDb?.userFavDao()
    }

    fun removeFav(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userFavDao?.deletingFav(id)
        }
    }

    // using coroutine as we use suspend that working in background
    fun addToFav(id: Int, avatar: String, username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = UserFavorite(
                id,
                avatar,
                username,
            )
            userFavDao?.addIntoFavorite(user)
        }
    }

    suspend fun checkUserFav(id: Int) = userFavDao?.checkUserFav(id)
    // End Favorite Part


    // getting data for favorite list
    fun getFavUser(): LiveData<List<UserFavorite>>? = userFavDao?.getUserFav()

    fun getDetailUser(): LiveData<User> = detailUser

    fun setDetailUser(username: String) {
        val link = "${MainViewModel.url}/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "")

        client.addHeader("User-Agent", "request")
        client.get(
            link,
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray
                ) {
                    try {
                        // Parsing JSON
                        val result = String(responseBody)
                        Log.d("Parsing", result)
                        val responseObject = JSONObject(result)

                        val user = User()
                        user.id = responseObject.getInt("id")
                        user.username = responseObject.getString("login")
                        user.name = responseObject.getString("name")
                        user.avatar = responseObject.getString("avatar_url")
                        user.repository = responseObject.getString("public_repos")
                        user.followers = responseObject.getString("followers")
                        user.following = responseObject.getString("following")
                        user.company = responseObject.getString("company")
                        user.location = responseObject.getString("location")
                        detailUser.postValue(user)

                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Log.d("Exception", error?.message.toString())
                }
            })
    }
}
