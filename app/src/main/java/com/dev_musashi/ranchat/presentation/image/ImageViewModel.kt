package com.dev_musashi.ranchat.presentation.image

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.dev_musashi.ranchat.util.SaveImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val saveImage: SaveImage
): ViewModel() {

    fun onImageSaveButtonClicked(uri: String){
        saveImage.saveImage(imageUri = uri.toUri())
    }

}