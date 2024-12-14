package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalAdministrativoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal_administrativo)

        val btnListadoPaciente = findViewById<Button>(R.id.btn_listado_paciente)
        val btnRegistroPaciente = findViewById<Button>(R.id.btn_registro_paciente)
        val btnRegistrarMedicos = findViewById<Button>(R.id.btn_registrar_medicos)
        val btnCerrarSesion = findViewById<Button>(R.id.btn_cerrar_sesion)

        btnListadoPaciente.setOnClickListener {
            val intent = Intent(this, ListadoPacientesActivity::class.java)
            startActivity(intent)
        }

        btnRegistroPaciente.setOnClickListener {
            val intent = Intent(this, RegistroPacienteActivity::class.java)
            startActivity(intent)
        }

        btnRegistrarMedicos.setOnClickListener {
            val intent = Intent(this, RegistrarMedicosActivity::class.java)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            // Cerrar sesión
        }
    }
}
