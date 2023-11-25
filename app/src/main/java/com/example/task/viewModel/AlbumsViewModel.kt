package com.example.task.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.model.AlbumsModel
import com.example.task.network.MyApi
import kotlinx.coroutines.launch

class AlbumsViewModel:ViewModel() {
    var albumsListResponse:List<AlbumsModel> by mutableStateOf(listOf())
    var errorMessage :String by mutableStateOf("")

    fun getAlbumsList(){
        viewModelScope.launch() {

            val apiService=MyApi.getInstance()
            try {
                val albumsList = apiService.getAlbums()
                albumsListResponse = albumsList
            }
            catch (e: Exception) {
                errorMessage = e.message.toString()
            }


        }
    }


}