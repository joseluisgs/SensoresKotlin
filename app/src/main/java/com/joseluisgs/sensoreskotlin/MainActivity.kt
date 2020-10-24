package com.joseluisgs.sensoreskotlin

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

// Lectura recomendada
// https://developer.android.com/guide/topics/sensors/sensors_overview
class MainActivity : AppCompatActivity(), SensorEventListener {
    // Mis variables
    private var mSensorManager: SensorManager? = null
    private var mAcelerometro: Sensor? = null
    var lienzo: LienzoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtenemos la referencia al servicio
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Nos centramos en el acelerómetro
        mAcelerometro = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // Preparamos la ventana
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Versión del SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_FULLSCREEN
            View.SYSTEM_UI_FLAG_IMMERSIVE
        }

        // set the view
        lienzo = LienzoView(this)
        setContentView(lienzo)
    }

    // Si cambia la preciśon
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // Si cambia el sensoor
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            // Actualizamos
            lienzo!!.updateMe(event.values[1], event.values[0])
        }
    }

    // AL volver, volvemos a analizarlos
    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(
            this, mAcelerometro,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    // AL pausar, dejamos de analizarlos
    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }


}