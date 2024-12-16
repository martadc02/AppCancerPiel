package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.firestore

class RegistroPacienteActivity : AppCompatActivity() {

    private lateinit var btnRegistro: Button
    private lateinit var auth: FirebaseAuth

    private lateinit var telefono: EditText
    private lateinit var letreroSex: TextView
    private lateinit var email: EditText
    private lateinit var fechaNac: EditText
    private lateinit var nombre: EditText
    private lateinit var apellidos: EditText
    private lateinit var dni: EditText
    private lateinit var gruposexo: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_paciente)

        // Inicialización de los componentes
        btnRegistro = findViewById(R.id.botonRegistro)
        auth = FirebaseAuth.getInstance()


        telefono = findViewById(R.id.editTextPhone)
        letreroSex = findViewById(R.id.textViewSex)
        email = findViewById(R.id.correoinfopersonal)
        nombre = findViewById(R.id.nombreinfopersonal)
        apellidos = findViewById(R.id.apellidosinfopersonal)
        fechaNac = findViewById(R.id.fechanacimientoinfopersonal)
        dni = findViewById(R.id.editdni)
        gruposexo = findViewById(R.id.radioGroupsexo)

        setup()
    }

    private fun setup() {
        btnRegistro.setOnClickListener {
            // Validar campos obligatorios
            if (email.text.isBlank() || nombre.text.isBlank() || apellidos.text.isBlank() ||
                fechaNac.text.isBlank() || telefono.text.isBlank() || dni.text.isBlank()
            ) {
                showAlert("Por favor, complete todos los campos")
                return@setOnClickListener
            }

            // Validar formato de email
            if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                showAlert("Por favor, ingrese un correo electrónico válido")
                return@setOnClickListener
            }

            // Validar formato de DNI
            if (!dni.text.toString().matches(Regex("\\d{8}[A-HJ-NP-TV-Z]"))) {
                showAlert("Por favor, ingrese un DNI válido")
                return@setOnClickListener
            }

            // Registrar el usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(
                email.text.toString(),
                "defaultPassword123" // Contraseña por defecto
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // Datos del paciente
                        val paciente = hashMapOf(
                            "nombre" to nombre.text.toString(),
                            "apellidos" to apellidos.text.toString(),
                            "fechaNacimiento" to fechaNac.text.toString(),
                            "telefono" to telefono.text.toString(),
                            "dni" to dni.text.toString(),
                            "sexo" to when (gruposexo.checkedRadioButtonId) {
                                R.id.radioButtonFemale -> "Femenino"
                                R.id.radioButtonMale -> "Masculino"
                                else -> "No especificado"
                            },
                            "email" to email.text.toString()
                        )

                        // Guardar en Firestore
                        val db = Firebase.firestore
                        db.collection("Pacientes").document(currentUser.uid)
                            .set(paciente)
                            .addOnSuccessListener {
                                Log.i("INFO", "Información del paciente registrada correctamente")
                                showSuccessDialog()
                            }
                            .addOnFailureListener { e ->
                                Log.e("ERROR", "Error al registrar información del paciente", e)
                                showAlert("Error al guardar la información del paciente")
                            }
                    }
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> "El correo ya está registrado"
                        else -> task.exception?.localizedMessage ?: "Error desconocido"
                    }
                    Log.e("ERROR", "Error al registrar usuario: $errorMessage", task.exception)
                    showAlert(errorMessage)
                }
            }
        }
    }

    // Mostrar un mensaje de éxito y redirigir
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso")
            .setMessage("El paciente se ha registrado correctamente.")
            .setPositiveButton("Aceptar") { _, _ ->
                // Redirigir a la página principal del administrador
                val intent = Intent(this, HomeAdminActivity::class.java)
                startActivity(intent)
                finish()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Mostrar alerta de error
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Botón para volver a la página principal del administrador
    fun onClickVolver(v: View?) {
        when (v?.id) {
            R.id.botonVolver -> {
                val intent = Intent(this, HomeAdminActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
