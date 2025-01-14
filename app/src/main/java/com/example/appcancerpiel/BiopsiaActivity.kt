package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class BiopsiaActivity : AppCompatActivity() {

    private lateinit var tipoEscisional: CheckBox
    private lateinit var tipoIncisional: CheckBox
    private lateinit var tumorPrimario: CheckBox
    private lateinit var tumorRecidivante: CheckBox
    private lateinit var prioridadUrgente: CheckBox
    private lateinit var prioridadNoUrgente: CheckBox
    private lateinit var localizacionLesion: EditText
    private lateinit var descripcionLesion: EditText
    private lateinit var botonGuardar: Button
    private lateinit var textViewNombrePaciente: TextView

    // Instancia de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biopsia)

        // Inicializar vistas
        tipoEscisional = findViewById(R.id.checkboxEscisional)
        tipoIncisional = findViewById(R.id.checkboxIncisional)
        tumorPrimario = findViewById(R.id.checkboxPrimario)
        tumorRecidivante = findViewById(R.id.checkboxRecidivante)
        prioridadUrgente = findViewById(R.id.checkboxUrgente)
        prioridadNoUrgente = findViewById(R.id.checkboxNoUrgente)
        localizacionLesion = findViewById(R.id.inputLocalizacion)
        descripcionLesion = findViewById(R.id.inputDescripcion)
        botonGuardar = findViewById(R.id.botonGuardar)
        textViewNombrePaciente = findViewById(R.id.nombrePacienteBiopsia)

        // Obtener los datos del Intent
        val nombre = intent.getStringExtra("NOMBRE") ?: "Nombre no disponible"
        val apellidos = intent.getStringExtra("APELLIDOS") ?: "Apellidos no disponibles"

        // Asignar el texto al TextView
        textViewNombrePaciente.text = "$nombre $apellidos"

        // Configurar acción del botón Guardar
        botonGuardar.setOnClickListener {
            guardarDatosYVolver()
        }
    }

    private fun guardarDatosYVolver() {
        // Validar datos antes de continuar
        if (!validarCampos()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un mapa con los datos de la biopsia
        val datosBiopsia = hashMapOf(
            "tipoEscisional" to tipoEscisional.isChecked,
            "tipoIncisional" to tipoIncisional.isChecked,
            "tumorPrimario" to tumorPrimario.isChecked,
            "tumorRecidivante" to tumorRecidivante.isChecked,
            "prioridadUrgente" to prioridadUrgente.isChecked,
            "prioridadNoUrgente" to prioridadNoUrgente.isChecked,
            "localizacionLesion" to localizacionLesion.text.toString(),
            "descripcionLesion" to descripcionLesion.text.toString(),
            "completado" to true // Indicamos que la prueba está completada
        )

        // Obtener el ID del paciente desde el Intent
        val pacienteId = intent.getStringExtra("PACIENTE_ID") ?: ""
        val nombre = intent.getStringExtra("NOMBRE") ?: "Nombre no disponible"
        val apellidos = intent.getStringExtra("APELLIDOS") ?: "Apellidos no disponibles"

        if (pacienteId.isEmpty()) {
            Toast.makeText(this, "Error: ID del paciente no encontrado.", Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar los datos en Firebase
        db.collection("Biopsias")
            .add(datosBiopsia)
            .addOnSuccessListener {
                // Actualizar el estado de la biopsia en la colección del paciente
                db.collection("Pacientes").document(pacienteId)
                    .update("biopsiaCompletada", true)
                    .addOnSuccessListener {
                        // Datos guardados correctamente
                        Toast.makeText(this, "Datos guardados correctamente.", Toast.LENGTH_SHORT).show()

                        // Redirigir a PruebasPendientesActivity con datos actualizados
                        val intent = Intent(this, PruebasPendientesActivity::class.java)
                        intent.putExtra("PACIENTE_ID", pacienteId)
                        intent.putExtra("NOMBRE", nombre)
                        intent.putExtra("APELLIDOS", apellidos)
                        intent.putExtra("Biopsia", false) // Biopsia ya completada
                        intent.putExtra("ValoracionResultados", true) // Activar valoración de resultados

                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al actualizar el estado: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validarCampos(): Boolean {
        return tipoEscisional.isChecked || tipoIncisional.isChecked &&
                (tumorPrimario.isChecked || tumorRecidivante.isChecked) &&
                (prioridadUrgente.isChecked || prioridadNoUrgente.isChecked) &&
                localizacionLesion.text.isNotEmpty() &&
                descripcionLesion.text.isNotEmpty()
    }
}
