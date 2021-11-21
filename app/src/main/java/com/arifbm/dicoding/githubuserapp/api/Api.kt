package com.arifbm.dicoding.githubuserapp.api

import com.arifbm.dicoding.githubuserapp.data.model.DetailUserResponse
import com.arifbm.dicoding.githubuserapp.data.model.User
import com.arifbm.dicoding.githubuserapp.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
            @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
            @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
            @Path("username") username: String
    ): Call<ArrayList<User>>
    
    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
            @Path("username") username: String
    ): Call<ArrayList<User>>

}