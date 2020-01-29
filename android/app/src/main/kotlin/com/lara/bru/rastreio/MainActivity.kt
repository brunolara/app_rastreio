package com.lara.bru.rastreio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant
import androidx.lifecycle.LifecycleRegistry
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.Lifecycle
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.lang.reflect.Method


class MainActivity: FlutterActivity() {
    var intentAux : Intent = Intent()

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        intentAux = Intent(context,MyService::class.java)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.lara.bru.rastreio.messages")
                .setMethodCallHandler {
                    call, result ->

                    if(call.method.equals("startService")){
                        startService()
                        result.success("service startado")
                    }
                }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(intentAux);
    }

    private fun startService(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            print("teste")
            startForegroundService(intentAux)
        }
        else {
            print("teste2")
            startService(intentAux)
        }
    }

}
