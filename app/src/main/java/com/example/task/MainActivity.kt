@file:OptIn(ExperimentalFoundationApi::class)

package com.example.task

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController


import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

import com.example.task.model.myUserData
import com.example.task.network.MyApi
import com.example.task.view.AppNavigation
import com.example.task.view.ProgressDialog
import com.example.task.viewModel.AlbumsViewModel
import com.example.task.viewModel.PhotoViewModel
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val BASE_URL="https://jsonplaceholder.typicode.com/"
    val albumsViewModel by viewModels<AlbumsViewModel>()
    val photoViewModel by viewModels<PhotoViewModel>()
    var show:Boolean=true

    var randomUser: myUserData? =null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()


            ProgressDialog(isVisible = show)
            getRandomUsers()
            ProgressDialog(isVisible = show)
            AppNavigation(
                randomUser = randomUser,
                albumsViewModel = albumsViewModel.albumsListResponse,
                userId= randomUser?.id,photoViewModel.photoListResponse,
            )
            albumsViewModel.getAlbumsList()
            photoViewModel.getPhotoList()

        }
    }



    private fun getRandomUsers(){
        val randomInt = Random.nextInt(1, 10)
        val api=Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)

        api.getUsers().enqueue(object : Callback<List<myUserData>>{
            override fun onResponse(
                call: Call<List<myUserData>>,
                response: Response<List<myUserData>>
            ) {
                if (response.isSuccessful){

                    randomUser=response.body()?.get(randomInt)
                    show=false
                }
            }
            override fun onFailure(call: Call<List<myUserData>>, t: Throwable) {
                Log.e("user",""+t.message)
            }
        })

    }
}




