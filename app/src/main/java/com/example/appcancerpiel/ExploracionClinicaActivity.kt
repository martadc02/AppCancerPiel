package com.example.appcancerpiel

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ExploracionClinicaActivity : AppCompatActivity() {

    private lateinit var checkboxLesionesCrecimiento: CheckBox
    private lateinit var checkboxUlceraCuracion: CheckBox
    private lateinit var checkboxPustulaHemorragica: CheckBox
    private lateinit var checkboxLesionesCicatrizales: CheckBox
    private lateinit var checkboxLesionCicatriz: CheckBox
    private lateinit var checkboxLesionHiperqueratosica: CheckBox
    private lateinit var checkboxLesionQueratocica: CheckBox
    private lateinit var checkboxQuemaduras: CheckBox
    private lateinit var botonGuardar: Button
    private lateinit var nombrePacienteView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exploracion_clinica)

        // Obtener datos del intent
        val nombrePaciente = intent.getStringExtra("nombrePaciente") ?: "Paciente"
        val pacienteId = intent.getStringExtra("pacienteId")

        // Inicializar vistas
        checkboxLesionesCrecimiento = findViewById(R.id.checkbox_lesiones_crecimiento)
        checkboxUlceraCuracion = findViewById(R.id.checkbox_ulcera_curacion)
        checkboxPustulaHemorragica = findViewById(R.id.checkbox_pustula_hemorragica)
        checkboxLesionesCicatrizales = findViewById(R.id.checkbox_lesiones_cicatrizales)
        checkboxLesionCicatriz = findViewById(R.id.checkbox_lesion_cicatriz)
        checkboxLesionHiperqueratosica = findViewById(R.id.checkbox_lesion_hiperqueratosica)
        checkboxLesionQueratocica = findViewById(R.id.checkbox_lesion_queratosica)
        checkboxQuemaduras = findViewById(R.id.checkbox_quemaduras)
        botonGuardar = findViewById(R.id.boton_guardar_exploracion)
        nombrePacienteView = findViewById(R.id.nombre_paciente)

        // Mostrar el nombre del paciente
        nombrePacienteView.text = nombrePaciente

        // Configurar el botón Guardar
        botonGuardar.setOnClickListener {
            guardarExploracionClinica(pacienteId)
        }
    }

    private fun guardarExploracionClinica(pacienteId: String?) {
        // Crear un mapa con los datos seleccionados
        val exploracion = hashMapOf(
            "pacienteId" to pacienteId,
            "lesionesCrecimiento" to checkboxLesionesCrecimiento.isChecked,
            "ulceraCuracion" to checkboxUlceraCuracion.isChecked,
            "pustulaHemorragica" to checkboxPustulaHemorragica.isChecked,
            "lesionesCicatrizales" to checkboxLesionesCicatrizales.isChecked,
            "lesionCicatriz" to checkboxLesionCicatriz.isChecked,
            "lesionHiperqueratosica" to checkboxLesionHiperqueratosica.isChecked,
            "lesionQueratocica" to checkboxLesionQueratocica.isChecked,
            "quemaduras" to checkboxQuemaduras.isChecked
        )

        // Guardar en Firebase
        db.collection("ExploracionesClinicas")
            .add(exploracion)
            .addOnSuccessListener {
                Toast.makeText(this, "Exploración clínica guardada correctamente.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
