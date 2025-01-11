package com.example.appcancerpiel

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallePacienteActivity : AppCompatActivity() {
    private lateinit var textViewTitulo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_paciente)

        textViewTitulo = findViewById(R.id.textViewTitulo)

        // Obtener el nombre y apellidos del paciente desde el Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")

        // Mostrar el nombre y apellidos en el título
        textViewTitulo.text = "Paciente: $nombre $apellidos"
    }
}