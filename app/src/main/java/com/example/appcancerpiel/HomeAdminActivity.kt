package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HomeAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)
    }

    fun onClick(v: View?){
        when(v?.id){
            R.id.btn_registro_paciente -> {
                startActivity(Intent(this, RegistroPacienteActivity::class.java))

            }
            R.id.btn_listado_paciente->{
                startActivity(Intent(this, ListadoPacientesActivity::class.java))
            }
            R.id.btn_registrar_medicos -> {
                startActivity(Intent(this, RegistrarMedicosActivity::class.java))
            }
            else -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
