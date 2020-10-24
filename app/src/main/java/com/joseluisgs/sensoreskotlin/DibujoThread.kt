package com.joseluisgs.sensoreskotlin

import android.graphics.Canvas
import android.view.SurfaceHolder

// Clase para el manejo del hilo
class DibujoThread(surfaceHolder: SurfaceHolder, panel: LienzoView) : Thread() {
    private var superficeHolder: SurfaceHolder? = null
    private var panel: LienzoView? = null
    private var run = false

    // Iniciación
    init {
        this.superficeHolder = surfaceHolder
        this.panel = panel
    }

    // Nos dice si se esta ejecutando
    fun setRunning(run: Boolean) {
        this.run = run
    }

    // Método Run, sincronizamos los datos
    override fun run() {
        var c: Canvas? = null
        while (run) {
            c = null
            try {
                c = superficeHolder!!.lockCanvas(null)
                synchronized(superficeHolder!!) {
                    panel!!.draw(c)
                }
            } finally {
                if (c != null) {
                    superficeHolder!!.unlockCanvasAndPost(c)
                }
            }
        }
    }
}


