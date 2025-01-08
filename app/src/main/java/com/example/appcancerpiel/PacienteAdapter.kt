package com.example.appcancerpiel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcancerpiel.modelo.Paciente

class PacienteAdapter(private val listaPacientes: List<Paciente>) :
    RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder>() {

    // ViewHolder para representar cada paciente en el RecyclerView
    inner class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textNombrePaciente)
        val email: TextView = itemView.findViewById(R.id.textEmailPaciente)
    }

    // Inflar el layout item_paciente para cada ítem del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paciente, parent, false)
        return PacienteViewHolder(view)
    }

    // Asociar los datos del paciente a las vistas en el ViewHolder
    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = listaPacientes[position]
        holder.nombre.text = "${paciente.nombre} ${paciente.apellidos}"
        holder.email.text = paciente.email
    }

    // Obtener el tamaño de la lista
    override fun getItemCount(): Int {
        return listaPacientes.size
    }
}
