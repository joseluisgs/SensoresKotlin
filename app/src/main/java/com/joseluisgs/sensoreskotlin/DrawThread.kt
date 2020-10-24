package com.joseluisgs.sensoreskotlin

import android.graphics.Canvas
import android.view.SurfaceHolder

// Clase para el manejo del hilo
class DrawThread(surfaceHolder: SurfaceHolder, panel: GroundView) : Thread() {
    private var surfaceHolder: SurfaceHolder? = null
    private var panel: GroundView? = null
    private var run = false

    // Iniciación
    init {
        this.surfaceHolder = surfaceHolder
        this.panel = panel
    }

    // Nos dice si se esta ejecutando
    fun setRunning(run: Boolean) {
        this.run = run
    }

    // Método Run
    override fun run() {
        var c: Canvas? = null
        while (run) {
            c = null
            try {
                c = surfaceHolder!!.lockCanvas(null)
                synchronized(surfaceHolder!!) {
                    panel!!.draw(c)
                }
            } finally {
                if (c != null) {
                    surfaceHolder!!.unlockCanvasAndPost(c)
                }
            }
        }
    }
}


