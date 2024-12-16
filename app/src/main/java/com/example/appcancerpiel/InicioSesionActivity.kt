package com.example.appcancerpiel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class InicioSesionActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var passwd: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var btnAcceder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        email = findViewById(R.id.editEmail)
        passwd = findViewById(R.id.editPassword)
        auth = Firebase.auth
        btnAcceder = findViewById(R.id.buttonIniciarSesion)

        // Función donde haremos la lógica de la autenticación
        setup()
    }

    private fun setup() {
        // Lógica de autenticación
        btnAcceder.setOnClickListener {
            val emailInput = email.text.toString().trim()
            val passwordInput = passwd.text.toString().trim()

            if (emailInput.isNotBlank() && passwordInput.isNotBlank()) {
                auth.signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("INFO", "Usuario logueado correctamente")
                            showHome(emailInput)
                            email.text.clear()
                            passwd.text.clear()
                        } else {
                            showAlert("Error logueando el usuario: ${task.exception?.localizedMessage}")
                        }
                    }
            } else {
                showAlert("Por favor, completa todos los campos")
            }
        }
    }

    private fun showHome(email: String) {
        val cleanEmail = email.trim()
        val db = Firebase.firestore

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.e("AUTH", "Error: Usuario no autenticado")
            showAlert("Error: Usuario no autenticado")
            return
        }

        // Verificar si es Administrador
        db.collection("Administrador")
            .whereEqualTo("email", cleanEmail)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    startActivity(Intent(this, HomeAdminActivity::class.java))
                    Log.i("INFO", "Usuario identificado como Administrador")
                } else {
                    // Verificar si es Médico si no es Administrador
                    verificarMedico(cleanEmail)
                }
            }
            .addOnFailureListener { exception ->
                showAlert("Error al verificar permisos: ${exception.message}")
                Log.e("ERROR", "Error al consultar Firestore: ", exception)
            }
    }

    private fun verificarMedico(email: String) {
        val db = Firebase.firestore

        db.collection("Medicos")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    startActivity(Intent(this, HomeMedicoActivity::class.java))
                    Log.i("INFO", "Usuario identificado como Médico")
                } else {
                    showAlert("Acceso denegado: No tienes permisos para acceder.")
                    Log.e("INFO", "Usuario no encontrado en Administrador ni Medicos")
                }
            }
            .addOnFailureListener { exception ->
                showAlert("Error al verificar permisos: ${exception.message}")
                Log.e("ERROR", "Error al consultar Firestore: ", exception)
            }
    }

    private fun showAlert(message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setTitle("Error")
            .setPositiveButton("ACEPTAR", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun onClickTipo(v: View?) {
        when (v?.id) {
            R.id.buttonCambioSesion -> {
                intent = Intent(this, TipoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
