package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
            "descripcionLesion" to descripcionLesion.text.toString()
        )

        // Guardar los datos en Firebase (colección "Biopsias")
        db.collection("Biopsias")
            .add(datosBiopsia)
            .addOnSuccessListener {
                // Datos guardados correctamente
                Toast.makeText(this, "Datos guardados correctamente.", Toast.LENGTH_SHORT).show()

                // Redirigir a PruebasPendientesActivity
                val intent = Intent(this, PruebasPendientesActivity::class.java)
                intent.putExtra("Reevaluacion", false)
                intent.putExtra("Iconografia", false)
                intent.putExtra("Biopsia", true)
                intent.putExtra("ValoracionResultados", true) // Hacer visible el nuevo botón
                startActivity(intent)

                finish() // Cerrar la actividad actual
            }
            .addOnFailureListener { e ->
                // Error al guardar los datos
                Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validarCampos(): Boolean {
        return !(tipoEscisional.isChecked || tipoIncisional.isChecked).not() &&
                !(tumorPrimario.isChecked || tumorRecidivante.isChecked).not() &&
                !(prioridadUrgente.isChecked || prioridadNoUrgente.isChecked).not() &&
                localizacionLesion.text.isNullOrEmpty().not() &&
                descripcionLesion.text.isNullOrEmpty().not()
    }
}