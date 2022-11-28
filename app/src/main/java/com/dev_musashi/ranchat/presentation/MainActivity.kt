package com.dev_musashi.ranchat.presentation

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.dev_musashi.ranchat.presentation.ui.theme.RanChatTheme
import com.dev_musashi.ranchat.util.SnackBarManager
import com.dev_musashi.ranchat.util.SocketController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST = 1000
        private const val WRITE_EXTERNAL_STORAGE_REQUEST = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this,TaskService::class.java))

        requestPermission.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        SocketController.init()

        setContent {
            RanChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Contents()
                }
            }
        }

        if (!SocketController.mSocket.connected()) {
            SnackBarManager.showMessage("연결이 해제되었습니다. 앱을 재실행 해주시길 바랍니다.")
        }

    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.forEach { boolean ->
            if(!boolean.value){
                CoroutineScope(Dispatchers.Default).launch {
                    SnackBarManager.showMessage("권한이 필요합니다. 설정에서 권한 사용 후 다시 시도해주시기 바랍니다.")
                    delay(2000)
                    ActivityCompat.finishAffinity(this@MainActivity)
                    exitProcess(0)
                }

            }
        }
    }

}
