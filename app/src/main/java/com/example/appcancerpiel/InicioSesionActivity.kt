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
import com.example.appcancerpiel.modelo.Alert
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

    fun setup() {
        // Aquí pondremos la lógica de los botones de autenticación

        btnAcceder.setOnClickListener{
            if(email.text.isNotBlank()&& passwd.text.isNotBlank()){
                auth.signInWithEmailAndPassword(
                    email.text.toString(),
                    passwd.text.toString()
                ).addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Log.i("INFO", "Usuario logueado correctamente")
                        showHome(email.text.toString())
                        email.text.clear()
                        passwd.text.clear()
                    }else{
                        showAlert("Error logueando el usuario")
                    }

                }
            }
        }
    }

    private fun showHome(email: String){
        //startActivity(Intent(this, HomePaciente::class.java))
        Firebase.firestore.collection("Administrador").document(email).get().addOnSuccessListener {
            if(it.exists()){
                startActivity(Intent(this, HomeAdminActivity::class.java))
            }
            else {
                startActivity(Intent(this, HomeMedicoActivity::class.java))

            }

        }

    }

    private fun showAlert(text:String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(text)
            .setTitle("Error")
            .setPositiveButton("ACEPTAR", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun onClickTipo(v: View?){
        when(v?.id){
            R.id.buttonCambioSesion -> {
                intent = Intent(this, TipoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}