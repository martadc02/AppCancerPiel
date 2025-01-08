package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class HomeMedicoActivity : AppCompatActivity() {

    private lateinit var btnListadoPacientes: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_medico)

        // Inicialización de botones
        btnListadoPacientes = findViewById(R.id.btnListadoPacientes)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        // Configurar botón de listado de pacientes
        btnListadoPacientes.setOnClickListener {
            val intent = Intent(this, ListadoPacientesActivity::class.java)
            startActivity(intent)
        }

        // Configurar botón de cerrar sesión
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        // Lógica para cerrar sesión
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, InicioSesionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

