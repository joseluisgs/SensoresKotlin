package com.joseluisgs.sensoreskotlin

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.os.Vibrator
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager

// Clase donde se dibujará todo
class GroundView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    // Cordenadas de la bola
    var cx: Float = 10.toFloat()
    var cy: Float = 10.toFloat()

    // incremento de la última posición
    var lastGx: Float = 0.toFloat()
    var lastGy: Float = 0.toFloat()

    // Tamaño de la bola
    var picHeight: Int = 0
    var picWidth: Int = 0

    var icon: Bitmap? = null

    // Tamaño de la ventana

    var Windowwidth: Int = 0
    var Windowheight: Int = 0

    // Hemos llegado al borde

    var noBorderX = false
    var noBorderY = false

    var vibratorService: Vibrator? = null
    var thread: DrawThread? = null


    init {
        holder.addCallback(this)
        // Creamos el hilo
        thread = DrawThread(holder, this)
        // obtenemos las referencias a los objetos a usar
        val display: Display = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size: Point = Point()
        display.getSize(size)
        Windowwidth = size.x
        Windowheight = size.y
        icon = BitmapFactory.decodeResource(resources, R.drawable.ball)
        picHeight = icon!!.height
        picWidth = icon!!.width
        // Utilizamos sl servicio de vibración

        vibratorService = (getContext().getSystemService(Service.VIBRATOR_SERVICE)) as Vibrator
    }

    override fun surfaceChanged(p0: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        thread!!.setRunning(true)
        thread!!.start()
    }

    // Pintamos el Canvas (lienzo)
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            canvas.drawColor(0xFFAAAAA)
            canvas.drawBitmap(icon, cx, cy, null)
        }
    }

    public override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            canvas.drawColor(0xFFAAAAA)
            canvas.drawBitmap(icon!!, cx, cy, null)
        }
    }

    // función que actualiza
    fun updateMe(inx: Float, iny: Float) {
        lastGx += inx
        lastGy += iny

        cx += lastGx
        cy += lastGy

        if (cx > (Windowwidth - picWidth)) {
            cx = (Windowwidth - picWidth).toFloat()
            lastGx = 0F
            if (noBorderX) {
                vibratorService!!.vibrate(100)
                noBorderX = false
            }
        } else if (cx < (0)) {
            cx = 0F
            lastGx = 0F
            if (noBorderX) {
                vibratorService!!.vibrate(100)
                noBorderX = false
            }
        } else {
            noBorderX = true
        }

        if (cy > (Windowheight - picHeight)) {
            cy = (Windowheight - picHeight).toFloat()
            lastGy = 0F
            if (noBorderY) {
                vibratorService!!.vibrate(100)
                noBorderY = false
            }
        } else if (cy < (0)) {
            cy = 0F
            lastGy = 0F
            if (noBorderY) {
                vibratorService!!.vibrate(100)
                noBorderY = false
            }
        } else {
            noBorderY = true
        }

        invalidate()

    }
}

