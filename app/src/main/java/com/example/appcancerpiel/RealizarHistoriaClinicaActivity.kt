package com.example.appcancerpiel

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RealizarHistoriaClinicaActivity : AppCompatActivity() {

    private lateinit var colorPiel: EditText
    private lateinit var colorPelo: EditText
    private lateinit var colorOjos: EditText
    private lateinit var checkboxExposicionSolar: CheckBox
    private lateinit var checkboxRadiacionesIonizantes: CheckBox
    private lateinit var checkboxConsumoAlcohol: CheckBox
    private lateinit var checkboxConsumoTabaco: CheckBox
    private lateinit var botonGuardar: Button
    private lateinit var nombrePacienteView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realizar_historia_clinica)

        // Obtener datos del Intent
        val nombrePaciente = intent.getStringExtra("nombrePaciente") ?: "Paciente"
        val pacienteId = intent.getStringExtra("pacienteId")

        // Inicializar vistas
        colorPiel = findViewById(R.id.color_piel)
        colorPelo = findViewById(R.id.color_pelo)
        colorOjos = findViewById(R.id.color_ojos)
        checkboxExposicionSolar = findViewById(R.id.checkbox_exposicion_solar)
        checkboxRadiacionesIonizantes = findViewById(R.id.checkbox_radiaciones_ionizantes)
        checkboxConsumoAlcohol = findViewById(R.id.checkbox_consumo_alcohol)
        checkboxConsumoTabaco = findViewById(R.id.checkbox_consumo_tabaco)
        botonGuardar = findViewById(R.id.boton_guardar_historia)
        nombrePacienteView = findViewById(R.id.nombre_paciente)

        // Mostrar el nombre del paciente
        nombrePacienteView.text = nombrePaciente

        // Configurar el botón Guardar
        botonGuardar.setOnClickListener {
            guardarHistoriaClinica(pacienteId)
        }
    }

    private fun guardarHistoriaClinica(pacienteId: String?) {
        // Obtener datos ingresados por el usuario
        val colorPielTexto = colorPiel.text.toString()
        val colorPeloTexto = colorPelo.text.toString()
        val colorOjosTexto = colorOjos.text.toString()

        // Validar campos obligatorios
        if (colorPielTexto.isBlank() || colorPeloTexto.isBlank() || colorOjosTexto.isBlank()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un mapa con los datos
        val historiaClinica = hashMapOf(
            "pacienteId" to pacienteId,
            "colorPiel" to colorPielTexto,
            "colorPelo" to colorPeloTexto,
            "colorOjos" to colorOjosTexto,
            "exposicionSolar" to checkboxExposicionSolar.isChecked,
            "radiacionesIonizantes" to checkboxRadiacionesIonizantes.isChecked,
            "consumoAlcohol" to checkboxConsumoAlcohol.isChecked,
            "consumoTabaco" to checkboxConsumoTabaco.isChecked
        )

        // Guardar en Firebase Firestore
        db.collection("HistoriasClinicas")
            .add(historiaClinica)
            .addOnSuccessListener {
                Toast.makeText(this, "Historia clínica guardada correctamente.", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
