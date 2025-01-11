package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

        // Inicialización de vistas
        textViewTitulo = findViewById(R.id.textViewTitulo)
        btnHistoriaClinica = findViewById(R.id.btnHistoriaClinica)
        btnExploracionClinica = findViewById(R.id.btnExploracionClinica)
        btnDermatoscopia = findViewById(R.id.btnDermatoscopia)
        btnValoracionResultados = findViewById(R.id.btnValoracionResultados)

        // Botones dinámicos
        val btnLesionSospechosa: Button = findViewById(R.id.btnLesionSospechosa)
        val btnLesionNoSospechosa: Button = findViewById(R.id.btnLesionNoSospechosa)

        // Obtener el nombre, apellidos y ID del paciente desde el Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")
        val pacienteId = intent.getStringExtra("PACIENTE_ID") // Opcional

        // Configurar el título de la actividad
        textViewTitulo.text = "Primeras Pruebas del Paciente: $nombre $apellidos"

        // Configurar el botón de Historia Clínica
        btnHistoriaClinica.setOnClickListener {
            val intent = Intent(this, RealizarHistoriaClinicaActivity::class.java)
            intent.putExtra("nombrePaciente", nombre)
            intent.putExtra("apellidosPaciente", apellidos)
            startActivity(intent)
        }

        // Configurar el botón de Exploración Clínica
        btnExploracionClinica.setOnClickListener {
            val intent = Intent(this, ExploracionClinicaActivity::class.java)
            intent.putExtra("nombrePaciente", nombre)
            intent.putExtra("apellidosPaciente", apellidos)
            startActivity(intent)
        }

        // Configurar el botón de Dermatoscopia
        btnDermatoscopia.setOnClickListener {
            val intent = Intent(this, DermatoscopiaActivity::class.java)
            intent.putExtra("nombrePaciente", nombre)
            intent.putExtra("apellidosPaciente", apellidos)
            startActivity(intent)
        }

        // Configurar el botón de Valoración de Resultados
        btnValoracionResultados.setOnClickListener {
            // Mostrar botones dinámicos
            btnLesionSospechosa.visibility = View.VISIBLE
            btnLesionNoSospechosa.visibility = View.VISIBLE

            // Configurar botón "Lesión Sospechosa"
            btnLesionSospechosa.setOnClickListener {
                // Ir a EleccionDermatologoActivity con nombre y apellidos del paciente
                if (nombre.isNullOrEmpty() || apellidos.isNullOrEmpty()) {
                    Toast.makeText(this, "Error: Información del paciente no disponible.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val intent = Intent(this, EleccionDermatologoActivity::class.java)
                intent.putExtra("nombrePaciente", nombre)
                intent.putExtra("apellidosPaciente", apellidos)
                startActivity(intent)
            }

            // Configurar botón "Lesión No Sospechosa"
            btnLesionNoSospechosa.setOnClickListener {
                Toast.makeText(this, "Lesión No Sospechosa seleccionada.", Toast.LENGTH_SHORT).show()
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
                val intent = Intent(this, HomeMedicoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
