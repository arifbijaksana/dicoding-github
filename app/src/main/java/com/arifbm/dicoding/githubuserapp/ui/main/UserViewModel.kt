package com.arifbm.dicoding.githubuserapp.ui.main

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arifbm.dicoding.githubuserapp.api.RetrofitClient
import com.arifbm.dicoding.githubuserapp.data.model.User
import com.arifbm.dicoding.githubuserapp.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(query: String){

        RetrofitClient.apiInstance.getSearchUser(query).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful){
                    listUser.postValue(response.body()?.items)
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.d("Failure", it) }
            }
        })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>>{

        return  listUser
    }
}