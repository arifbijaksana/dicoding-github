package com.arifbm.dicoding.githubuserapp.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.arifbm.dicoding.githubuserapp.data.local.FavoriteUser
import com.arifbm.dicoding.githubuserapp.data.local.FavoriteUserDao
import com.arifbm.dicoding.githubuserapp.data.local.UserDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabases(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>?{
        return userDao?.getFavoriteUser()
    }
}