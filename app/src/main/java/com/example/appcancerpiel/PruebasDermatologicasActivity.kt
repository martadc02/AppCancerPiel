package com.example.appcancerpiel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class PruebasDermatologicasActivity : AppCompatActivity() {

    private lateinit var checkBoxReevaluacion: CheckBox
    private lateinit var checkBoxIconografia: CheckBox
    private lateinit var checkBoxBiopsia: CheckBox
    private lateinit var botonEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas_dermatologicas)

        // Inicialización de vistas
        checkBoxReevaluacion = findViewById(R.id.checkboxReevaluacion)
        checkBoxIconografia = findViewById(R.id.checkboxIconografia)
        checkBoxBiopsia = findViewById(R.id.checkboxBiopsia)
        botonEnviar = findViewById(R.id.botonEnviar)

        // Configurar el botón Enviar
        botonEnviar.setOnClickListener {
            val intent = Intent(this, PruebasPendientesActivity::class.java)
            // Pasar el estado de las pruebas seleccionadas
            intent.putExtra("Reevaluacion", checkBoxReevaluacion.isChecked)
            intent.putExtra("Iconografia", checkBoxIconografia.isChecked)
            intent.putExtra("Biopsia", checkBoxBiopsia.isChecked)
            startActivity(intent)
        }
    }
}