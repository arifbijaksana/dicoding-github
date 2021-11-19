package com.arifbm.dicoding.githubuserapp.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arifbm.dicoding.githubuserapp.api.RetrofitClient
import com.arifbm.dicoding.githubuserapp.data.local.FavoriteUser
import com.arifbm.dicoding.githubuserapp.data.local.FavoriteUserDao
import com.arifbm.dicoding.githubuserapp.data.local.UserDatabase
import com.arifbm.dicoding.githubuserapp.data.model.DetailUserResponse
import com.arifbm.dicoding.githubuserapp.data.model.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.coroutines.coroutineContext

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserResponse>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabases(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {

        RetrofitClient.apiInstance.getUserDetail(username)
            .enqueue(object : retrofit2.Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username, id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }
    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun  removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }


}