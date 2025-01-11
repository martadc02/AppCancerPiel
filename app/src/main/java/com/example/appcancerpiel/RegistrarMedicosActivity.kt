package com.example.appcancerpiel

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem


class RegistrarMedicosActivity : AppCompatActivity() {

    // Declaración de variables para las vistas
    private lateinit var nombreMedico: EditText
    private lateinit var apellidosMedico: EditText
    private lateinit var emailMedico: EditText
    private lateinit var telefonoMedico: EditText
    private lateinit var passwordMedico: EditText
    private lateinit var radioGroupTipoMedico: RadioGroup
    private lateinit var btnRegistrarMedico: Button

    // Firebase
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_medicos)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Registrar Nuevo Médico" // Título de la Toolbar
        }

        // Inicialización de vistas
        nombreMedico = findViewById(R.id.editNombreMedico)
        apellidosMedico = findViewById(R.id.editApellidosMedico)
        emailMedico = findViewById(R.id.editEmailMedico)
        telefonoMedico = findViewById(R.id.editTelefonoMedico)
        passwordMedico = findViewById(R.id.editPasswordMedico)
        radioGroupTipoMedico = findViewById(R.id.radioGroupTipoMedico)
        btnRegistrarMedico = findViewById(R.id.btnRegistrarMedico)

        auth = FirebaseAuth.getInstance()

        // Configurar el botón
        btnRegistrarMedico.setOnClickListener {
            registrarMedico()
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
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun registrarMedico() {
        val email = emailMedico.text.toString().trim()
        val password = passwordMedico.text.toString().trim()
        val nombre = nombreMedico.text.toString().trim()
        val apellidos = apellidosMedico.text.toString().trim()
        val telefono = telefonoMedico.text.toString().trim()

        // Validación de campos
        if (email.isBlank() || password.isBlank() || nombre.isBlank() || apellidos.isBlank() || telefono.isBlank()) {
            showAlert("Por favor completa todos los campos.")
            return
        }

        if (password.length < 6) {
            showAlert("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        // Obtener el tipo de médico seleccionado
        val tipoMedico = when (radioGroupTipoMedico.checkedRadioButtonId) {
            R.id.radioMedicoFamilia -> "Médico de Familia"
            R.id.radioDermatologo -> "Dermatólogo"
            else -> {
                showAlert("Por favor selecciona un tipo de médico.")
                return
            }
        }

        // Crear cuenta en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid

                // Guardar datos adicionales del médico en Firestore
                val medico = hashMapOf(
                    "uid" to userId,
                    "nombre" to nombre,
                    "apellidos" to apellidos,
                    "email" to email,
                    "telefono" to telefono,
                    "tipo" to tipoMedico,
                    "rol" to "medico"
                )

                db.collection("Medicos").document(userId!!)
                    .set(medico)
                    .addOnSuccessListener {
                        showSuccessDialog("Médico registrado correctamente.\n\n" +
                                "Correo: $email\nContraseña: (proporcionada)")
                    }
                    .addOnFailureListener { e ->
                        showAlert("Error al guardar el médico: ${e.message}")
                        Log.e("Firestore", "Error al guardar datos: ${e.message}", e)
                    }
            }
            .addOnFailureListener { e ->
                showAlert("Error al registrar el médico: ${e.message}")
                Log.e("Auth", "Error en Firebase Auth: ${e.message}", e)
            }
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Registro")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .create()
            .show()
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Registro Exitoso")
            .setMessage(message)
            .setPositiveButton("Aceptar") { _, _ ->
                finish() // Cierra la actividad después del éxito
            }
            .create()
            .show()
    }
}

