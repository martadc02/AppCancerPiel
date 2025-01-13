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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class PrimerasPruebasActivity : AppCompatActivity() {

    private lateinit var textViewTitulo: TextView
    private lateinit var btnHistoriaClinica: Button
    private lateinit var btnExploracionClinica: Button
    private lateinit var btnDermatoscopia: Button
    private lateinit var btnValoracionResultados: Button
    private lateinit var btnLesionSospechosa: Button
    private lateinit var btnLesionNoSospechosa: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var isMedicoFamilia: Boolean = true // Variable para determinar el rol del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primeras_pruebas)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Primeras Pruebas"
        }

        // Inicialización de vistas
        textViewTitulo = findViewById(R.id.textViewTitulo)
        btnHistoriaClinica = findViewById(R.id.btnHistoriaClinica)
        btnExploracionClinica = findViewById(R.id.btnExploracionClinica)
        btnDermatoscopia = findViewById(R.id.btnDermatoscopia)
        btnValoracionResultados = findViewById(R.id.btnValoracionResultados)
        btnLesionSospechosa = findViewById(R.id.btnLesionSospechosa)
        btnLesionNoSospechosa = findViewById(R.id.btnLesionNoSospechosa)

        // Ocultar los botones dinámicos por defecto
        btnLesionSospechosa.visibility = View.GONE
        btnLesionNoSospechosa.visibility = View.GONE

        // Obtener datos del Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")
        val dni = intent.getStringExtra("DNI")

        // Configurar el título de la actividad
        textViewTitulo.text = "Primeras Pruebas del Paciente: $nombre $apellidos"

        // Verificar el rol del usuario y configurar botones dinámicamente
        val userId = auth.currentUser?.uid
        if (userId != null) {
            verificarRolYConfigurarBotones(userId, nombre, apellidos, dni)
        } else {
            Toast.makeText(this, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificarRolYConfigurarBotones(uid: String, nombre: String?, apellidos: String?, dni: String?) {
        db.collection("Medicos").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val tipo = document.getString("tipo")
                    isMedicoFamilia = tipo == "Médico de Familia"

                    configurarBotones(nombre, apellidos, dni)
                } else {
                    Toast.makeText(this, "Error: No se encontró el rol del usuario.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al verificar el rol del usuario.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun configurarBotones(nombre: String?, apellidos: String?, dni: String?) {
        // Configurar el botón de Historia Clínica
        btnHistoriaClinica.setOnClickListener {
            val intent = if (isMedicoFamilia) {
                Intent(this, RealizarHistoriaClinicaActivity::class.java)
            } else {
                Intent(this, VerHistoriaClinicaActivity::class.java)
            }
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            intent.putExtra("DNI", dni)
            startActivity(intent)
        }

        // Configurar el botón de Exploración Clínica
        btnExploracionClinica.setOnClickListener {
            val intent = if (isMedicoFamilia) {
                Intent(this, ExploracionClinicaActivity::class.java)
            } else {
                Intent(this, VerExploracionClinicaActivity::class.java)
            }
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            intent.putExtra("DNI", dni)
            startActivity(intent)
        }

        // Configurar el botón de Dermatoscopia
        btnDermatoscopia.setOnClickListener {
            val intent = if (isMedicoFamilia) {
                Intent(this, DermatoscopiaActivity::class.java)
            } else {
                Intent(this, VerDermatoscopiaActivity::class.java)
            }
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            intent.putExtra("DNI", dni)
            startActivity(intent)
        }

        // Configurar la visibilidad y acción del botón de Valoración de Resultados
        if (isMedicoFamilia) {
            btnValoracionResultados.visibility = View.VISIBLE
            btnValoracionResultados.setOnClickListener {
                // Mostrar botones dinámicos
                btnLesionSospechosa.visibility = View.VISIBLE
                btnLesionNoSospechosa.visibility = View.VISIBLE

                // Configurar botón "Lesión Sospechosa"
                btnLesionSospechosa.setOnClickListener {
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
        } else {
            btnValoracionResultados.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_home -> {
                val intent = Intent(this, HomeMedicoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

