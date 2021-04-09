package com.example.github2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github2.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    val detailUser = MutableLiveData<User>()

    fun getDetailUser(): LiveData<User> {
        return detailUser
    }

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
