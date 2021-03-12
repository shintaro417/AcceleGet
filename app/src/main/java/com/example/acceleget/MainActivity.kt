package com.example.acceleget

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.acceleget.databinding.ActivityMainBinding

/**
 * SensorEventListener:加速度センサーの実装に必要
 */
class MainActivity : AppCompatActivity(),SensorEventListener {
    //onCreateメソッドで初期化したい変数はlateinit修飾子を使う
    private lateinit var binding: ActivityMainBinding
    //閾値
    private var threshold: Float = 10f
    //加速度センサーのZ軸の値を記憶する
    private var oldValue: Float = 0f
    private lateinit var cameraManager:CameraManager
    private var cameraID:String? = null
    //LEDがONかOffか
    private var lightOn:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
     * センサーが変更されたときに呼び出されるメソッド
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null)return
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            //Android端末が倒れたと判断したかどうか判定する
            val zDiff = Math.abs(event.values[2] - oldValue)
            //Z軸の値と保存しておいた値の差が閾値を超えた場合に実施
            if(zDiff > threshold){
                torchOn()
            }
            oldValue = event.values[2]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        //SesnsorManagerオブジェクを取得する
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //Sensorオブジェクトを取得する
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //イベントリスナーを登録する
        //this:リスナーオブジェクト センサーオブジェクト センサーからデータ取得するレート(定数かマイクロ秒で指定)
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //リスナーの登録を解除する。
        sensorManager.unregisterListener(this)

        if(cameraID != null){
            try {
                if (lightOn){
                    cameraManager.setTorchMode(cameraID!!,false)
                }
            }catch (e:CameraAccessException){
                e.printStackTrace()
            }
        }
    }

    /**
     * LEDを点灯、消灯
     */
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