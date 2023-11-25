package com.example.task.network


import com.example.task.model.AlbumsModel
import com.example.task.model.PhotoModel
import com.example.task.model.myUserData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET

interface MyApi {

    @GET("users")
    fun getUsers(): Call<List<myUserData>>


    @GET("albums")
    suspend fun getAlbums():List<AlbumsModel>

    @GET("photos")
    suspend fun getPhotos():List<PhotoModel>

    companion object{
        var apiService:MyApi?=null
        fun getInstance():MyApi{
            if (apiService==null){
                apiService=Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(MyApi::class.java);
            }
            return apiService!!
        }
    }

}