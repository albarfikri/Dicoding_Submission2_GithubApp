package com.example.github2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github2.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val listUser = MutableLiveData<ArrayList<User>>()
    private val searchLiveData = MutableLiveData<String>()
    private val availabilityState = MutableLiveData<Boolean>()

    companion object {
        const val url = "https://api.github.com"
    }

    fun getListUser(): LiveData<ArrayList<User>> = listUser


    fun getSearchLiveData(): LiveData<String> = searchLiveData

    fun getAvailabilityState(): LiveData<Boolean> = availabilityState

    fun setSearchLiveData(username: String) {
        searchLiveData.postValue(username)
    }

    fun setFollowerAndFollowing(username: String?, typeUser: String) {
        val listItems = ArrayList<User>()

        val link = "$url/users/$username/$typeUser"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "")

        client.addHeader("User-Agent", "request")
        client.get(link, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    if (responseArray.length() > 0) {

                        for (i in 0 until responseArray.length()) {
                            val item = responseArray.getJSONObject(i)
                            val user = User()
                            user.id = item.getInt("id")
                            user.username = item.getString("login")
                            user.avatar = item.getString("avatar_url")
                            listItems.add(user)
                        }
                        listUser.postValue(listItems)
                        availabilityState.postValue(true)
                    } else {
                        listUser.postValue(listItems)
                        availabilityState.postValue(false)
                    }

                } catch (e: Exception) {
                    Log.d("onFailure", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("Status Code", "$statusCode $errorMessage")
            }
        })
    }


    fun setDataByUsername(username: String) {
        val listItems = ArrayList<User>()

        val link = "$url/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "")

        client.addHeader("User-Agent", "request")
        client.get(link, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("Parsing", result)
                    val responseObject = JSONObject(result)
                    val count = responseObject.getInt("total_count")
                    if (count >= 1) {
                        val items = responseObject.getJSONArray("items")

                        for (i in 0 until items.length()) {
                            // taking an object sesuai urutan
                            val item = items.getJSONObject(i)
                            val user = User()
                            user.username = item.getString("login")
                            user.avatar = item.getString("avatar_url")
                            listItems.add(user)
                        }
                        listUser.postValue(listItems)
                        availabilityState.postValue(true)
                    } else {
                        availabilityState.postValue(false)
                        listUser.postValue(listItems)
                    }

                } catch (e: Exception) {
                    Log.d("onFailure", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("Status Code", "$statusCode $errorMessage")
            }

        })
    }

    fun getAll() {
        val listItems = ArrayList<User>()

        val link = "$url/users"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "")
        client.addHeader("User-Agent", "request")
        client.get(link, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("Berhasil Mengubah", result)
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userItems = User()
                        userItems.id = jsonObject.getInt("id")
                        userItems.avatar = jsonObject.getString("avatar_url")
                        userItems.username = jsonObject.getString("login")
                        listItems.add(userItems)
                    }
                    listUser.postValue(listItems)
                    availabilityState.postValue(true)
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
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("Status Code", "$statusCode $errorMessage")
            }
        })
    }
}