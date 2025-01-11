package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PrimerasPruebasActivity : AppCompatActivity() {
    private lateinit var textViewTitulo: TextView
    private lateinit var btnHistoriaClinica: Button
    private lateinit var btnExploracionClinica: Button
    private lateinit var btnDermatoscopia: Button
    private lateinit var btnValoracionResultados: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primeras_pruebas)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Primeras Pruebas" // Título de la Toolbar
        }

        textViewTitulo = findViewById(R.id.textViewTitulo)
        btnHistoriaClinica = findViewById(R.id.btnHistoriaClinica)
        btnExploracionClinica = findViewById(R.id.btnExploracionClinica)
        btnDermatoscopia = findViewById(R.id.btnDermatoscopia)
        btnValoracionResultados = findViewById(R.id.btnValoracionResultados)

        // Obtener el nombre y apellidos del paciente desde el Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")

        // Configurar el título de la actividad
        textViewTitulo.text = "Primeras Pruebas del Paciente: $nombre $apellidos"

        // Configurar los botones según sea necesario
        btnHistoriaClinica.setOnClickListener {
            // botón de Historia Clínica
        }

        btnExploracionClinica.setOnClickListener {
            // botón de Exploración Clínica
        }

        btnDermatoscopia.setOnClickListener {
            // botón de Dermatóscopia
        }

        btnValoracionResultados.setOnClickListener {
            // Obtener referencias a los nuevos botones
            val btnLesionSospechosa: Button = findViewById(R.id.btnLesionSospechosa)
            val btnLesionNoSospechosa: Button = findViewById(R.id.btnLesionNoSospechosa)

            // Cambiar la visibilidad de los botones a "VISIBLE"
            btnLesionSospechosa.visibility = View.VISIBLE
            btnLesionNoSospechosa.visibility = View.VISIBLE

            // Configurar listeners para los nuevos botones
            btnLesionSospechosa.setOnClickListener {
                // Acción para "Lesión Sospechosa"
            }

            btnLesionNoSospechosa.setOnClickListener {
                // Acción para "Lesión No Sospechosa"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu) // Inflar el menú si existe
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Botón "Atrás" en la Toolbar
                finish()
                true
            }
            R.id.action_home -> { // Icono de Home en el menú
                val intent = Intent(this, HomeMedicoActivity::class.java) // Crear el Intent para abrir HomeMedicoActivity
                startActivity(intent) // Iniciar la nueva actividad
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}