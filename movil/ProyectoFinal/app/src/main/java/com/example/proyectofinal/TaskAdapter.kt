package com.example.proyectofinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para mostrar la lista de tareas en un RecyclerView.
 *
 * @param tasks Lista mutable de objetos Task.
 * @param onDeleteClick Función lambda a ejecutar al hacer clic en Borrar. Recibe el ID de la tarea.
 * @param onModifyClick Función lambda a ejecutar al hacer clic en Modificar. Recibe el objeto Task.
 */
class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDeleteClick: (Int?) -> Unit,
    private val onModifyClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    /**
     * ViewHolder que mantiene las referencias a los elementos de la interfaz de cada tarea.
     */
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.taskTitleTextView)
        val dateTextView: TextView = view.findViewById(R.id.taskDateTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.taskDescriptionTextView)

        val expandedLayout: LinearLayout = view.findViewById(R.id.expandedLayout)
        val toggleDescriptionButton: Button = view.findViewById(R.id.toggleDescriptionButton)
        val modifyButton: Button = view.findViewById(R.id.modifyButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]

        // Asignación de datos a la vista
        holder.titleTextView.text = currentTask.title
        holder.dateTextView.text = "Fecha: ${currentTask.deliveryDate}"
        holder.descriptionTextView.text = currentTask.description

        // 1. Lógica de Despliegue/Contracción
        val isExpanded = currentTask.isExpanded
        holder.expandedLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.toggleDescriptionButton.text = if (isExpanded) "Ver Menos" else "Desplegar"

        holder.toggleDescriptionButton.setOnClickListener {
            // Alternar el estado isExpanded en el modelo de datos
            currentTask.isExpanded = !currentTask.isExpanded
            // Notificar que este ítem ha cambiado para redibujar
            notifyItemChanged(position)
        }

        // 2. Manejo de Botón Borrar
        holder.deleteButton.setOnClickListener {
            onDeleteClick(currentTask.id)
            // Actualizar la lista localmente
            tasks.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, tasks.size) // Para asegurar que las posiciones se actualicen
        }

        // 3. Manejo de Botón Modificar
        holder.modifyButton.setOnClickListener {
            onModifyClick(currentTask)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    /**
     * Función auxiliar para actualizar la lista de tareas desde la Activity.
     */
    fun refreshData(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}