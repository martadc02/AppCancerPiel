package com.example.appcancerpiel

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PruebasPendientesActivity : AppCompatActivity() {

    private lateinit var botonReevaluacion: Button
    private lateinit var botonIconografia: Button
    private lateinit var botonBiopsia: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas_pendientes)

        // Inicialización de vistas
        botonReevaluacion = findViewById(R.id.botonReevaluacion)
        botonIconografia = findViewById(R.id.botonIconografia)
        botonBiopsia = findViewById(R.id.botonBiopsia)

        // Obtener los datos pasados desde PruebasDermatologicasActivity
        val reevaluacion = intent.getBooleanExtra("Reevaluacion", false)
        val iconografia = intent.getBooleanExtra("Iconografia", false)
        val biopsia = intent.getBooleanExtra("Biopsia", false)

        // Configurar visibilidad de los botones según las pruebas seleccionadas
        botonReevaluacion.visibility = if (reevaluacion) View.VISIBLE else View.GONE
        botonIconografia.visibility = if (iconografia) View.VISIBLE else View.GONE
        botonBiopsia.visibility = if (biopsia) View.VISIBLE else View.GONE

        // Configurar acciones de los botones (Opcional)
        botonReevaluacion.setOnClickListener {
            // Acción para Reevaluación
        }

        botonIconografia.setOnClickListener {
            // Acción para Iconografía
        }

        botonBiopsia.setOnClickListener {
            // Acción para Biopsia
        }
    }
}
