package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistrarMedicosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_medicos)
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