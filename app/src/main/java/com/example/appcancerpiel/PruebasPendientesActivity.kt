package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.util.Log


class PruebasPendientesActivity : AppCompatActivity() {

    private lateinit var botonBiopsia: Button
    private lateinit var botonValoracionResultados: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas_pendientes)

        // Inicialización de vistas
        botonBiopsia = findViewById(R.id.botonBiopsia)
        botonValoracionResultados = findViewById(R.id.botonValoracionResultados)

        // Obtener los datos pasados desde la actividad previa
        val biopsia = intent.getBooleanExtra("Biopsia", false)
        val valoracionResultados = intent.getBooleanExtra("ValoracionResultados", false)
        val pacienteId = intent.getStringExtra("PACIENTE_ID") ?: ""
        val nombre = intent.getStringExtra("NOMBRE") ?: "Nombre no disponible"
        val apellidos = intent.getStringExtra("APELLIDOS") ?: "Apellidos no disponibles"

        // Configurar visibilidad de los botones según las pruebas seleccionadas
        botonBiopsia.visibility = if (biopsia) View.VISIBLE else View.GONE
        botonValoracionResultados.visibility = if (valoracionResultados) View.VISIBLE else View.GONE

        // Configurar acciones de los botones
        botonBiopsia.setOnClickListener {
            val intent = Intent(this, BiopsiaActivity::class.java)
            intent.putExtra("PACIENTE_ID", pacienteId)
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            startActivity(intent)
        }

        botonValoracionResultados.setOnClickListener {
            // Añade este log para verificar los valores
            Log.i("PruebasPendientesActivity", "Nombre: $nombre, Apellidos: $apellidos, ID: $pacienteId")

            // Configurar el intent para iniciar ValoracionResultadosActivity
            val intent = Intent(this, ValoracionResultadosActivity::class.java)
            intent.putExtra("PACIENTE_ID", pacienteId)
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            startActivity(intent)
        }
    }
}
