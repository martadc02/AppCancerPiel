package com.example.appcancerpiel

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PruebasDermatologicasActivity : AppCompatActivity() {

    private lateinit var textViewTitulo: TextView
    private lateinit var btnReevaluacion: CheckBox
    private lateinit var btnIconografia: CheckBox
    private lateinit var btnBiopsia: CheckBox
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas_dermatologicas)

        // Inicialización de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Habilitar botón "Atrás"
            title = "Solicitud de Pruebas" // Título de la Toolbar
        }

        // Inicialización de vistas
        textViewTitulo = findViewById(R.id.textViewTitulo)
        btnReevaluacion = findViewById(R.id.checkBoxReevaluacion)
        btnIconografia = findViewById(R.id.checkBoxIconografia)
        btnBiopsia = findViewById(R.id.checkBoxBiopsia)
        btnEnviar = findViewById(R.id.btnEnviar)

        // Obtener el nombre y apellidos del paciente desde el Intent
        val nombre = intent.getStringExtra("NOMBRE")
        val apellidos = intent.getStringExtra("APELLIDOS")

        // Configurar el título de la actividad
        textViewTitulo.text = "$nombre $apellidos"

        // Configurar el botón "Enviar"
        btnEnviar.setOnClickListener {
            val reevaluacion = btnReevaluacion.isChecked
            val iconografia = btnIconografia.isChecked
            val biopsia = btnBiopsia.isChecked

            if (!reevaluacion && !iconografia && !biopsia) {
                Toast.makeText(this, "Seleccione al menos una opción.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí podrías enviar los datos a tu backend o base de datos
            Toast.makeText(this, "Solicitud enviada con éxito.", Toast.LENGTH_SHORT).show()

            // Finalizar la actividad
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
