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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.example.appcancerpiel.modelo.Alert
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import java.text.SimpleDateFormat
import com.google.firebase.auth.ktx.auth

class RegistroPacienteActivity : AppCompatActivity() {

    private lateinit var btnRegistro : Button
    private lateinit var auth: FirebaseAuth

    private lateinit var telefono: EditText
    private lateinit var letreroSex: TextView
    private lateinit var email: EditText
    private lateinit var fechaNac : EditText
    private lateinit var nombre : EditText
    private lateinit var apellidos : EditText
    private lateinit var gruposexo : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_paciente)

        btnRegistro = findViewById(R.id.botonRegistro)
        auth = com.google.firebase.ktx.Firebase.auth

        telefono = findViewById(R.id.editTextPhone)
        letreroSex= findViewById(R.id.textViewSex)
        email = findViewById(R.id.correoinfopersonal)
        nombre = findViewById(R.id.nombreinfopersonal)
        apellidos = findViewById(R.id.apellidosinfopersonal)
        fechaNac = findViewById(R.id.fechanacimientoinfopersonal)
        gruposexo = findViewById(R.id.radioGroupsexo)

        setup()

    }

    fun setup() {
        btnRegistro.setOnClickListener {
            // Validar campos
            if (email.text.isBlank() || nombre.text.isBlank() || apellidos.text.isBlank() ||
                fechaNac.text.isBlank() || telefono.text.isBlank()
            ) {
                Alert.showAlert(this, "Por favor, complete todos los campos")

            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                Alert.showAlert(this, "Por favor, ingrese un correo electrónico válido")

            }

            // Intentar registrar al usuario
            auth.createUserWithEmailAndPassword(
                email.text.toString(),
                "defaultPassword123"
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val paciente = hashMapOf(
                            "nombre" to nombre.text.toString(),
                            "apellidos" to apellidos.text.toString(),
                            "fechaNacimiento" to fechaNac.text.toString(),
                            "telefono" to telefono.text.toString(),
                            "sexo" to when (gruposexo.checkedRadioButtonId) {
                                R.id.radioButtonFemale -> "Femenino"
                                R.id.radioButtonMale -> "Masculino"
                                else -> "No especificado"
                            },
                            "email" to email.text.toString()
                        )
                        val db = Firebase.firestore
                        db.collection("Pacientes").document(currentUser.uid)
                            .set(paciente)
                            .addOnSuccessListener {
                                Log.i("INFO", "Información del paciente registrada correctamente")
                            }
                            .addOnFailureListener { e ->
                                Log.e("ERROR", "Error al registrar información del paciente", e)
                                Alert.showAlert(this, "Error al guardar la información del paciente")
                            }
                    }
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> "El correo ya está registrado"
                        else -> task.exception?.localizedMessage ?: "Error desconocido"
                    }
                    Log.e("ERROR", "Error al registrar usuario: $errorMessage", task.exception)
                    Alert.showAlert(this, errorMessage)
                }
            }
        }
    }
    fun onClickVolver(v: View?){
        when(v?.id){
            R.id.botonVolver -> {
                intent = Intent(this, HomeAdminActivity::class.java)
                startActivity(intent)
            }
        }
    }

}