package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

class HomeAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    // Inflar el menú en la Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    // Manejar clics en el menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                // Acción para el ícono de home
                val intent = Intent(this, HomeAdminActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
