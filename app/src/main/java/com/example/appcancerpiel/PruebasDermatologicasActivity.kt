package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PruebasDermatologicasActivity : AppCompatActivity() {

    private lateinit var checkBoxReevaluacion: CheckBox
    private lateinit var checkBoxIconografia: CheckBox
    private lateinit var checkBoxBiopsia: CheckBox
    private lateinit var botonEnviar: Button
    private lateinit var textViewNombrePaciente: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas_dermatologicas)

        // Inicialización de vistas
        checkBoxReevaluacion = findViewById(R.id.checkboxReevaluacion)
        checkBoxIconografia = findViewById(R.id.checkboxIconografia)
        checkBoxBiopsia = findViewById(R.id.checkboxBiopsia)
        botonEnviar = findViewById(R.id.botonEnviar)
        textViewNombrePaciente = findViewById(R.id.textViewNombrePaciente)

        // Obtener los datos del Intent
        val nombre = intent.getStringExtra("NOMBRE") ?: getString(R.string.nombre)
        val apellidos = intent.getStringExtra("APELLIDOS") ?: getString(R.string.apellido)
        val pacienteId = intent.getStringExtra("PACIENTE_ID") ?: "ID no disponible"

        // Mostrar el nombre del paciente debajo del título
        val pacienteText = "$nombre $apellidos"
        textViewNombrePaciente.text = pacienteText

        // Verificar que el ID del paciente no esté vacío
        if (pacienteId == "ID no disponible") {
            Toast.makeText(this, getString(R.string.error_paciente_id), Toast.LENGTH_LONG).show()
            Log.e("PruebasDermatologicas", getString(R.string.error_paciente_log))
            finish()
        }

        // Configurar el botón Enviar
        botonEnviar.setOnClickListener {
            val intent = Intent(this, PruebasPendientesActivity::class.java)

            // Pasar el estado de las pruebas seleccionadas
            intent.putExtra("Reevaluacion", checkBoxReevaluacion.isChecked)
            intent.putExtra("Iconografia", checkBoxIconografia.isChecked)
            intent.putExtra("Biopsia", checkBoxBiopsia.isChecked)

            // Pasar también el nombre, apellidos e ID del paciente
            intent.putExtra("NOMBRE", nombre)
            intent.putExtra("APELLIDOS", apellidos)
            intent.putExtra("PACIENTE_ID", pacienteId)

            // Mostrar mensaje de éxito
            Toast.makeText(this, getString(R.string.datos_enviados_exito), Toast.LENGTH_SHORT).show()

            // Iniciar la nueva actividad
            startActivity(intent)
        }
    }
}
