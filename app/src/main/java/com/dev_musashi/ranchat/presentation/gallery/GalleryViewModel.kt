package com.dev_musashi.ranchat.presentation.gallery

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_musashi.ranchat.domain.Repository
import com.dev_musashi.ranchat.util.QueryImages
import com.dev_musashi.ranchat.util.Screen
import com.dev_musashi.ranchat.util.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val queryImages: QueryImages,
    private val repository: Repository
) : ViewModel() {

    var state = mutableStateOf(GalleryState())
        private set

    private val images get() = state.value.images
    private val selectedImageUri get() = state.value.imageUri
    private val isSelectedState get() = state.value.isSelectedState

    private var currentIndex: Int? = null
    private var _nickname: String? = null
    private var _roomName: String? = null

    fun init(nickname: String, roomName: String){
        _nickname = nickname
        _roomName = roomName
        loadImages()
        state.value = state.value.copy(init = false)
    }

    private fun loadImages() {
        viewModelScope.launch {
            val queryImages = queryImages.queryImages()
            state.value = state.value.copy(images = queryImages)
        }
    }

    fun onImageSelected(imageUri: String, newIndex: Int){
        if(currentIndex==null) {
            currentIndex = newIndex
            state.value.isSelectedState[newIndex] = true
            state.value = state.value.copy(imageUri = imageUri)
        } else {
            if(currentIndex == newIndex) {
                state.value.isSelectedState[currentIndex!!] = !state.value.isSelectedState[currentIndex]!!
                state.value = state.value.copy(imageUri = "")
                currentIndex = null
            } else {
                state.value.isSelectedState[currentIndex!!] = false
                currentIndex = newIndex
                state.value.isSelectedState[newIndex] = true
                state.value = state.value.copy(imageUri = imageUri)
            }
        }
    }

    fun onImageTouch(image: String, open:(String)->Unit) {
        open(Screen.ImageScreen.passImage(image))
    }

    fun onImageSend(popUp: () -> Unit){
        viewModelScope.launch {
            isSelectedState.forEach {
                if(it.value) {
                    state.value.isSelectedState[it.key] = false
                }
            }
            repository.uploadImage(selectedImageUri, _roomName!!, _nickname!!) { error ->
                if(error!= null) {
                    SnackBarManager.showMessage("사진 업로드에 실패하였습니다. 잠시후 다시 시도 해주시길 바랍니다.")
                }
            }
            currentIndex = null
            popUp()
        }

    }

    fun popUp(popUp: ()->Unit) {
        currentIndex = 0
        popUp()
    }

}