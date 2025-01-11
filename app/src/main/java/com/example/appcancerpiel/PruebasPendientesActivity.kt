package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PruebasPendientesActivity : AppCompatActivity() {

    private lateinit var botonReevaluacion: Button
    private lateinit var botonIconografia: Button
    private lateinit var botonBiopsia: Button
    private lateinit var botonValoracionResultados: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas_pendientes)

        // Inicialización de vistas
        botonReevaluacion = findViewById(R.id.botonReevaluacion)
        botonIconografia = findViewById(R.id.botonIconografia)
        botonBiopsia = findViewById(R.id.botonBiopsia)
        botonValoracionResultados = findViewById(R.id.botonValoracionResultados)

        // Obtener los datos pasados desde la actividad previa
        val reevaluacion = intent.getBooleanExtra("Reevaluacion", false)
        val iconografia = intent.getBooleanExtra("Iconografia", false)
        val biopsia = intent.getBooleanExtra("Biopsia", false)
        val valoracionResultados = intent.getBooleanExtra("ValoracionResultados", false)

        // Configurar visibilidad de los botones según las pruebas seleccionadas
        botonReevaluacion.visibility = if (reevaluacion) View.VISIBLE else View.GONE
        botonIconografia.visibility = if (iconografia) View.VISIBLE else View.GONE
        botonBiopsia.visibility = if (biopsia) View.VISIBLE else View.GONE
        botonValoracionResultados.visibility = if (valoracionResultados) View.VISIBLE else View.GONE

        // Configurar acciones de los botones
        botonReevaluacion.setOnClickListener {
            // Acción para Reevaluación
        }

        botonIconografia.setOnClickListener {
            // Acción para Iconografía
        }

        botonBiopsia.setOnClickListener {
            // Navegar a BiopsiaActivity
            val intent = Intent(this, BiopsiaActivity::class.java)
            startActivity(intent)
        }

        botonValoracionResultados.setOnClickListener {
            // Acción para Valoración de Resultados
        }
    }
}
