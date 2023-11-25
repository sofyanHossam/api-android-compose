package com.example.task.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.model.PhotoModel
import com.example.task.network.MyApi
import kotlinx.coroutines.launch

class PhotoViewModel: ViewModel() {
    var photoListResponse:List<PhotoModel> by mutableStateOf(listOf())
    var errorMessage :String by mutableStateOf("")

    fun getPhotoList(){
        viewModelScope.launch {
            val apiService=MyApi.getInstance()
            try {
                val photoList = apiService.getPhotos()
                photoListResponse = photoList
            }
            catch (e: Exception) {
                errorMessage = e.message.toString()
            }

        }
    }


}