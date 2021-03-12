package com.example.acceleget

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.acceleget.databinding.ActivityMainBinding

/**
 * SensorEventListener:加速度センサーの実装に必要
 */
class MainActivity : AppCompatActivity(),SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * センサーが変更されたときに呼び出されるメソッド
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null)return
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            binding.txt01.text="x軸:${event.values[0]}\nY軸:${event.values[1]}\nZ軸:${event.values[2]}"
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
    }
}