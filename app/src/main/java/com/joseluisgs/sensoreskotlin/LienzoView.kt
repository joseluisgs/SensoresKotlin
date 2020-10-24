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
class LienzoView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    // Cordenadas de la bola
    var bolaPosX: Float = 10.toFloat()
    var bolaPosY: Float = 10.toFloat()

    // incremento de la última posición
    var ultimaPosX: Float = 0.toFloat()
    var ultimaPosY: Float = 0.toFloat()

    // Tamaño de la bola
    var bolaAltura: Int = 0
    var bolaAnchura: Int = 0

    var bola: Bitmap? = null

    // Tamaño de la ventana

    var Windowwidth: Int = 0
    var Windowheight: Int = 0

    // Hemos llegado al borde
    var bordeX = false
    var bordeY = false

    var vibracionService: Vibrator? = null
    var thread: DibujoThread? = null


    init {
        holder.addCallback(this)
        // Creamos el hilo
        thread = DibujoThread(holder, this)
        // obtenemos las referencias a los objetos a usar
        val display: Display = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size: Point = Point()
        display.getSize(size)
        Windowwidth = size.x
        Windowheight = size.y
        bola = BitmapFactory.decodeResource(resources, R.drawable.ball)
        bolaAltura = bola!!.height
        bolaAnchura = bola!!.width
        // Utilizamos sl servicio de vibración

        vibracionService = (getContext().getSystemService(Service.VIBRATOR_SERVICE)) as Vibrator
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
        if (canvas != null) {
            super.draw(canvas)
            canvas.drawColor(0xFFAAAAA)
            canvas.drawBitmap(bola!!, bolaPosX, bolaPosY, null)
        }
    }

    public override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            canvas.drawColor(0xFFAAAAA)
            canvas.drawBitmap(bola!!, bolaPosX, bolaPosY, null)
        }
    }

    // función que actualiza
    fun updateMe(inx: Float, iny: Float) {
        ultimaPosX += inx
        ultimaPosY += iny

        bolaPosX += ultimaPosX
        bolaPosY += ultimaPosY

        if (bolaPosX > (Windowwidth - bolaAnchura)) {
            bolaPosX = (Windowwidth - bolaAnchura).toFloat()
            ultimaPosX = 0F
            if (bordeX) {
                vibracionService!!.vibrate(100)
                bordeX = false
            }
        } else if (bolaPosX < (0)) {
            bolaPosX = 0F
            ultimaPosX = 0F
            if (bordeX) {
                vibracionService!!.vibrate(100)
                bordeX = false
            }
        } else {
            bordeX = true
        }

        if (bolaPosY > (Windowheight - bolaAltura)) {
            bolaPosY = (Windowheight - bolaAltura).toFloat()
            ultimaPosY = 0F
            if (bordeY) {
                vibracionService!!.vibrate(100)
                bordeY = false
            }
        } else if (bolaPosY < (0)) {
            bolaPosY = 0F
            ultimaPosY = 0F
            if (bordeY) {
                vibracionService!!.vibrate(100)
                bordeY = false
            }
        } else {
            bordeY = true
        }

        invalidate()

    }
}

