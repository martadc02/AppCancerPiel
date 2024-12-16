package com.example.appcancerpiel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcancerpiel.modelo.Paciente

class PacienteAdapter(private val pacientes: List<Paciente>) :
    RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder>() {

    // ViewHolder para mantener las referencias de las vistas
    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textNombre)
        val emailTextView: TextView = itemView.findViewById(R.id.textEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_paciente_adapter, parent, false)
        return PacienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = pacientes[position]
        holder.nombreTextView.text = "${paciente.nombre} ${paciente.apellidos}"
        holder.emailTextView.text = paciente.email
    }

    override fun getItemCount(): Int {
        return pacientes.size
    }
}
