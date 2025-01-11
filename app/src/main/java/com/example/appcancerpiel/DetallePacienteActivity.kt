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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetallePacienteActivity : AppCompatActivity() {
    private lateinit var textViewTitulo: TextView
    private lateinit var btnDatosPaciente: Button
    private lateinit var btnPrimerasPruebas: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_paciente)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Detalles" // Título de la Toolbar
        }

        textViewTitulo = findViewById(R.id.textViewTitulo)
        btnDatosPaciente = findViewById(R.id.btnDatosPaciente)
        btnPrimerasPruebas = findViewById(R.id.btnPrimerasPruebas)

        // Obtener el nombre y apellidos del paciente desde el Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")

        // Mostrar el nombre y apellidos en el título
        textViewTitulo.text = "Paciente: $nombre $apellidos"

        // Obtener el UID del usuario actualmente autenticado
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            // Verificar el rol del usuario en Firebase
            verificarRolYMostrarBotones(uid)
        }

        // Configurar el click para el botón de Primeras Pruebas
        btnPrimerasPruebas.setOnClickListener {
            // Usamos los datos obtenidos previamente
            val intent = Intent(this, PrimerasPruebasActivity::class.java)
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            startActivity(intent)
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

    // Verifica el rol del usuario y muestra los botones según corresponda
    private fun verificarRolYMostrarBotones(uid: String) {
        db.collection("Medicos").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val rol = document.getString("rol")
                    if (rol == "medico") {
                        // Si el usuario es médico, mostrar ambos botones
                        btnDatosPaciente.visibility = View.VISIBLE
                        btnPrimerasPruebas.visibility = View.VISIBLE
                    }
                } else {
                    // Si no es médico, verificar si es administrador
                    db.collection("Administrador").document(uid)
                        .get()
                        .addOnSuccessListener { adminDocument ->
                            if (adminDocument.exists()) {
                                val rol = adminDocument.getString("rol")
                                if (rol == "admin") {
                                    // Si el usuario es administrador, mostrar solo el botón de "Datos del paciente"
                                    btnDatosPaciente.visibility = View.VISIBLE
                                    btnPrimerasPruebas.visibility = View.GONE
                                }
                            }
                        }
                }
            }
            .addOnFailureListener {
                // Manejar el error si no se pudo obtener el rol
            }
    }
}

