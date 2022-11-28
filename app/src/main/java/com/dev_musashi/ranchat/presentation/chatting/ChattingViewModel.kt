package com.dev_musashi.ranchat.presentation.chatting

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_musashi.ranchat.domain.Message
import com.dev_musashi.ranchat.domain.Repository
import com.dev_musashi.ranchat.util.QueryImages
import com.dev_musashi.ranchat.util.Screen
import com.dev_musashi.ranchat.util.SnackBarManager
import com.dev_musashi.ranchat.util.SocketController
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.emitter.Emitter
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val queryImages: QueryImages,
    private val repository: Repository
) : ViewModel() {

    private val mSocket = SocketController.mSocket

    var state = mutableStateOf(ChattingState())
        private set

    private val comment get() = state.value.comment
    private val messageList get() = state.value.messageList
    private val imageUri get() = state.value.imageUri
    private val isSelectedState get() = state.value.isSelectedState
    private val init get() = state.value.init

    private var timer: Timer? = null
    private var _nickname: String? = null
    private var _roomName: String? = null
    private var currentIndex: Int? = null

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    private val statusChangeComplete: Emitter.Listener = Emitter.Listener {
        if (it.isNotEmpty()) {
            startFinding(_nickname!!)
        }
    }

    private val findingComplete: Emitter.Listener = Emitter.Listener {
        timer?.cancel()
        _roomName = it[0].toString()
        val complete = Message("", "상대방이 입장하였습니다.")
        onMessageListChanged(newMessage = complete)
        state.value = state.value.copy(textField = true, refresh = false)
        state.value = state.value.copy(init = false)

    }

    private val messageListener : Emitter.Listener = Emitter.Listener { data ->
        val obj = JSONObject(data[0].toString())
        val nickname = obj.get("nickname").toString()
        val comment = obj.get("comment").toString()
        val message = Message(nickname, comment)
        onMessageListChanged(newMessage = message)
    }

    private val chatEnd : Emitter.Listener  = Emitter.Listener {
        onMessageListChanged(newMessage = Message(nickname = "", comment = "대화방이 종료되었습니다."))
        mSocket.emit("socketReset", _roomName)
        _roomName = null
        state.value = state.value.copy(textField = false, refresh = true)
    }

    private fun startFinding(nickname: String) {
        viewModelScope.launch {
            timer = timer(period = 500) {
                mSocket.emit("doFinding", nickname)
            }
        }
    }

    fun init(nickname: String) {
        if(init) {
            viewModelScope.launch {
                _nickname = nickname
                mSocket.on("statusChangeComplete", statusChangeComplete)
                mSocket.on("FindingComplete", findingComplete)
                mSocket.on("messageResult", messageListener)
                mSocket.on("chatEnd", chatEnd)
                onMessageListChanged(newMessage = Message(nickname = "", comment = "상대방을 찾는 중입니다."))
                mSocket.emit("statusFinding", _nickname)
            }

        }
    }

    fun onMessageChanged(newMessage: String) {
        state.value = state.value.copy(comment = newMessage)
    }

    private fun onMessageListChanged(newMessage: Message){
        val newList : MutableList<Message> = mutableListOf()
        messageList.reversed().forEach {
            newList.add(it)
        }
        newList.add(newMessage)
        state.value = state.value.copy(messageList = newList.asReversed())
    }

    fun onImageChanged(newImageUri: Uri?) {
        state.value = state.value.copy(imageUri = newImageUri.toString())
    }

    fun onBackBottomSheetClick(){
        isSelectedState.forEach {
            if(it.value) {
                state.value.isSelectedState[it.key] = false
            }
        }
    }

    fun onSendClick() {
        if (_roomName != null && _nickname != null && comment != "") {
            val messageGson = Message(nickname = _nickname!!, comment = comment)
            val messageJson = gson.toJson(messageGson)
            val data = JSONObject()
            data.put("roomName", _roomName)
            data.put("message", messageJson)
            viewModelScope.launch {
                mSocket.emit("message", data)
            }
            onMessageChanged("")
        }
    }

    fun openBottomSheetListGallery() {
        viewModelScope.launch {
            val images = queryImages.queryImages()
            state.value = state.value.copy(galleryImages = images)
        }
    }

    fun moveToGallery(open: (String) -> Unit) {
        if(_nickname != null && _roomName != null) {
            open(Screen.GalleryScreen.passNicknameAndRoomName(nickname = _nickname!!, roomName = _roomName!!))
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

    fun onImageSend(){
        viewModelScope.launch {
            isSelectedState.forEach {
                if(it.value) {
                    state.value.isSelectedState[it.key] = false
                }
            }
            repository.uploadImage(imageUri, _roomName!!, _nickname!!) { error ->
                if(error!= null) {
                    SnackBarManager.showMessage("사진 업로드에 실패하였습니다. 잠시후 다시 시도 해주시길 바랍니다.")
                }
            }
            currentIndex = null
        }
    }

    fun onImageTouch(image: String, open: (String)->Unit){
        open(Screen.ImageScreen.passImage(image))
    }

    fun openDialog(){
        state.value = state.value.copy(openDialog = true)
    }

    fun closeDialog(){
        state.value = state.value.copy(openDialog = false)
    }

    fun back(popUp: ()->Unit) {
        timer?.cancel()
        viewModelScope.launch {
            mSocket.emit("cancelFinding", _nickname)
            mSocket.off("FindingComplete", findingComplete)
            mSocket.off("statusChangeComplete", statusChangeComplete)
            mSocket.off("messageResult", messageListener)
            mSocket.emit("exit", _roomName)
            _roomName = null
        }
        popUp()
    }

    fun exit(){
        mSocket.emit("exit", _roomName)
    }

    fun refresh(){
        state.value = state.value.copy(messageList = emptyList())
        viewModelScope.launch { mSocket.emit("statusFinding", _nickname) }
        val finding = Message(nickname = "", comment = "상대방을 찾는 중입니다.")
        onMessageListChanged(newMessage = finding)
    }

    override fun onCleared() {
        super.onCleared()
        mSocket.emit("cancelFinding", _nickname)
        mSocket.emit("exit", _roomName)
    }

}

