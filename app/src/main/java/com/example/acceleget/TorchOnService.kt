package com.example.acceleget

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.acceleget.databinding.ActivityMainBinding

/**
 * Service():サービスの実装
 */
class TorchOnService:Service(),SensorEventListener {
    //閾値
    private var threshold: Float = 10f
    //加速度センサーのZ軸の値を記憶する
    private var oldValue: Float = 0f
    private lateinit var cameraManager: CameraManager
    private var cameraID:String? = null
    //LEDがONかOffか
    private var lightOn:Boolean = false
    //private val tag = "Bata Pika!"

    override fun onCreate() {
        super.onCreate()
        //Activity版と同じ処理
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        //トーチモードが変更されたときの処理を登録
        cameraManager.registerTorchCallback(object : CameraManager.TorchCallback(){
            //トーチモードが変更されたときに呼び出されるメソッド
            override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                super.onTorchModeChanged(cameraId, enabled)
                cameraID = cameraId
                lightOn = enabled
            }
        }, Handler(Looper.myLooper()!!))
    }

    /**
     * サービスが開始されたタイミングで呼び出されるメソッド
     * ActivityのonResumeメソッドと同じ
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        val sensorManager =getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL)
    }

    /**
     * アクティビティ版のonPauseメソッドと同じ処理内容
     */
    override fun onDestroy() {
        super.onDestroy()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)

        if(cameraID != null){
            try {
                if(lightOn){
                    cameraManager.setTorchMode(cameraID!!,false)
                }
            }catch (e:CameraAccessException){
                e.printStackTrace()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null)return
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val zDiff = Math.abs(event.values[2] - oldValue)
            if(zDiff > threshold){
                torchOn()
            }
            oldValue = event.values[2]
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun torchOn(){
        if(cameraID != null){
            try {
                if(!lightOn){
                    cameraManager.setTorchMode(cameraID!!,true)
                }else{
                    cameraManager.setTorchMode(cameraID!!,false)
                }
            }catch (e:CameraAccessException){
                e.printStackTrace()
            }
        }
    }
}