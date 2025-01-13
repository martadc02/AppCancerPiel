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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetallePacienteActivity : AppCompatActivity() {

    private lateinit var textViewTitulo: TextView
    private lateinit var btnDatosPaciente: Button
    private lateinit var btnPrimerasPruebas: Button
    private lateinit var btnPruebasDermatologo: Button
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
        btnPruebasDermatologo = findViewById(R.id.btnPruebasDermatologo)

        // Ocultar botones inicialmente
        btnPrimerasPruebas.visibility = View.GONE
        btnPruebasDermatologo.visibility = View.GONE

        // Obtener el nombre y apellidos del paciente desde el Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")
        val email = intent.getStringExtra("EMAIL")
        val telefono = intent.getStringExtra("TELEFONO")
        val dni = intent.getStringExtra("DNI")
        val fechaNacimiento = intent.getStringExtra("FECHA_NACIMIENTO")
        val sexo = intent.getStringExtra("SEXO")

        // Mostrar el nombre y apellidos en el título
        textViewTitulo.text = "Paciente: $nombre $apellidos"

        // Obtener el UID del usuario actualmente autenticado
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            verificarRolYMostrarBotones(uid)
        }

        // Botón Datos del paciente

        btnDatosPaciente.setOnClickListener {
            val intent = Intent(this, DatosPacienteActivity::class.java)
            intent.putExtra("NOMBRE", nombre)        // Envía el nombre
            intent.putExtra("APELLIDOS", apellidos)  // Envía los apellidos
            intent.putExtra("EMAIL", email)          // Envía el email
            intent.putExtra("TELEFONO", telefono)    // Envía el teléfono
            intent.putExtra("DNI", dni)              // Envía el DNI
            intent.putExtra("FECHA_NACIMIENTO", fechaNacimiento) // Envía la fecha de nacimiento
            intent.putExtra("SEXO", sexo)            // Envía el sexo

            // Inicia la actividad DatosPacienteActivity
            startActivity(intent)
        }


        // Configurar el click para el botón de Primeras Pruebas
        btnPrimerasPruebas.setOnClickListener {
            val intent = Intent(this, PrimerasPruebasActivity::class.java)
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            intent.putExtra("DNI", dni)

            startActivity(intent)
        }

        // Configurar el click para el botón de Pruebas Dermatólogo
        btnPruebasDermatologo.setOnClickListener {
            val intent = Intent(this, PruebasDermatologicasActivity::class.java)
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            intent.putExtra("DNI", dni)
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
                val intent = Intent(this, HomeMedicoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun verificarRolYMostrarBotones(uid: String) {
        db.collection("Medicos").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val tipo = document.getString("tipo")
                    if (tipo == "Dermatólogo") {
                        btnPrimerasPruebas.visibility = View.VISIBLE
                        btnPruebasDermatologo.visibility = View.VISIBLE
                    } else if (tipo == "Médico de Familia") {
                        btnPrimerasPruebas.visibility = View.VISIBLE
                        btnPruebasDermatologo.visibility = View.GONE
                    }
                } else {
                    db.collection("Administrador").document(uid)
                        .get()
                        .addOnSuccessListener { adminDocument ->
                            if (adminDocument.exists()) {
                                val rol = adminDocument.getString("rol")
                                if (rol == "admin") {
                                    btnPrimerasPruebas.visibility = View.GONE
                                    btnPruebasDermatologo.visibility = View.GONE
                                }
                                btnDatosPaciente.visibility = View.VISIBLE
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al verificar el rol del usuario.", Toast.LENGTH_SHORT).show()
            }
    }
}
