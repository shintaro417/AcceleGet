package com.example.acceleget

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.acceleget.databinding.ActivityMainBinding

/**
 * SensorEventListener:加速度センサーの実装に必要
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startBtn.setOnClickListener {
            val intent = Intent(this,TorchOnService::class.java)
            startService(intent)
        }
        binding.stopBtn.setOnClickListener {
            val intent = Intent(this,TorchOnService::class.java)
            stopService(intent)
        }

    }


//    /**
//     * センサーが変更されたときに呼び出されるメソッド
//     */
//    override fun onSensorChanged(event: SensorEvent?) {
//        if(event == null)return
//        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
//            //Android端末が倒れたと判断したかどうか判定する
//            val zDiff = Math.abs(event.values[2] - oldValue)
//            //Z軸の値と保存しておいた値の差が閾値を超えた場合に実施
//            if(zDiff > threshold){
//                torchOn()
//            }
//            oldValue = event.values[2]
//        }
//    }

//    override fun onResume() {
//        super.onResume()
//        //SesnsorManagerオブジェクを取得する
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        //Sensorオブジェクトを取得する
//        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        //イベントリスナーを登録する
//        //this:リスナーオブジェクト センサーオブジェクト センサーからデータ取得するレート(定数かマイクロ秒で指定)
//        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL)
//    }

//    override fun onPause() {
//        super.onPause()
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        //リスナーの登録を解除する。
//        sensorManager.unregisterListener(this)
//
//        if(cameraID != null){
//            try {
//                if (lightOn){
//                    cameraManager.setTorchMode(cameraID!!,false)
//                }
//            }catch (e:CameraAccessException){
//                e.printStackTrace()
//            }
//        }
//    }

//    override fun onBind(intent: Intent):IBinder?{
//        return null
//    }
//
//    /**
//     * LEDを点灯、消灯
//     */
//    private fun torchOn(){
//        if(cameraID != null){
//            try {
//                if(!lightOn){
//                    cameraManager.setTorchMode(cameraID!!,true)
//                }else{
//                    cameraManager.setTorchMode(cameraID!!,false)
//                }
//            }catch (e:CameraAccessException){
//                e.printStackTrace()
//            }
//        }
//    }
}