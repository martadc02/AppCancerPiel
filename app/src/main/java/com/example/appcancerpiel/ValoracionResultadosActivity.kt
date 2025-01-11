package com.example.appcancerpiel

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ValoracionResultadosActivity : AppCompatActivity() {

    private lateinit var btnCuidadosPaliativos: Button
    private lateinit var btnTratamientoMedico: Button
    private lateinit var btnCirugia: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valoracion_resultados)

        // Inicialización de botones
        btnCuidadosPaliativos = findViewById(R.id.btnCuidadosPaliativos)
        btnTratamientoMedico = findViewById(R.id.btnTratamientoMedico)
        btnCirugia = findViewById(R.id.btnCirugia)

        // Configurar acciones de los botones
        btnCuidadosPaliativos.setOnClickListener {
            Toast.makeText(this, "Seleccionado: Cuidados Paliativos", Toast.LENGTH_SHORT).show()
        }

        btnTratamientoMedico.setOnClickListener {
            Toast.makeText(this, "Seleccionado: Tratamiento Médico", Toast.LENGTH_SHORT).show()
        }

        btnCirugia.setOnClickListener {
            Toast.makeText(this, "Seleccionado: Cirugía", Toast.LENGTH_SHORT).show()
        }
    }
}
