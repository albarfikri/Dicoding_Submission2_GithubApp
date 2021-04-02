package com.example.github2.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github2.Model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUser = MutableLiveData<ArrayList<User>>()
    val searchLiveData = MutableLiveData<String>()
    val availabilityState = MutableLiveData<Boolean>()
    val url = "https://api.github.com"


    fun getListUser(): LiveData<ArrayList<User>> {
        return listUser
    }

    fun getSearchLiveData(): LiveData<String> = searchLiveData

    fun getAvailabilityState(): LiveData<Boolean> = availabilityState

    fun setSearchLiveData(username: String) {
        searchLiveData.postValue(username)
    }

    fun setDataByUsername(username: String) {
        val listItems = ArrayList<User>()

        val link = "$url/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(link, object : AsyncHttpResponseHandler() {
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
                // giving failure tag
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getAll() {
        val listItems = ArrayList<User>()

        val link = "$url/users"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(link, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    //parsing JSON
                    val result = String(responseBody)
                    Log.d("Berhasil Mengubah", result)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userItems = User()
                        userItems.username = jsonObject.getString("login")
                        userItems.avatar = jsonObject.getString("avatar_url")
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
                Log.d("onFailure", error?.message.toString())
            }
        })
    }
}