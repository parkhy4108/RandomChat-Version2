package com.dev_musashi.ranchat.presentation

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.dev_musashi.ranchat.util.SocketController

class TaskService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        SocketController.disconnect()
        stopSelf()
    }
}
