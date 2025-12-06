package com.example.proyectofinal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Activity que contiene la interfaz principal del panel de tareas.
 * Se encarga de gestionar el CRUD de tareas, filtrando los datos por el
 * ID del usuario actual de Firebase (Multi-Usuario).
 */
class PanelActivity : AppCompatActivity() {

    // Instancias de gestores
    private lateinit var authManager: AuthManager
    private lateinit var dbHelper: DatabaseHelper

    // ¡NUEVO! Variable para almacenar el ID del usuario logueado
    private var currentUserId: String? = null

    // Elementos de la UI
    private lateinit var userIdentifierTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addTaskButton: Button
    private lateinit var tasksRecyclerView: RecyclerView

    // Adaptador y lista de tareas
    private lateinit var taskAdapter: TaskAdapter
    private var taskList = mutableListOf<Task>()

    // Variable para manejar el modo de modificación (null = Agregar, !null = Modificar)
    private var taskToModify: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        // 1. Inicialización de Gestores y Referencias
        authManager = AuthManager.instance
        dbHelper = DatabaseHelper(this)

        // **PASO CRUCIAL 1**: Obtener el ID del usuario de Firebase.
        currentUserId = authManager.getCurrentUserId()
        if (currentUserId == null) {
            // Si por alguna razón no hay ID, forzamos el cierre de sesión.
            Toast.makeText(this, "Sesión inválida. Por favor, inicie sesión de nuevo.", Toast.LENGTH_LONG).show()
            authManager.logout()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        userIdentifierTextView = findViewById(R.id.userIdentifierTextView)
        logoutButton = findViewById(R.id.logoutButton)
        titleEditText = findViewById(R.id.titleEditText)
        dateEditText = findViewById(R.id.dateEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        addTaskButton = findViewById(R.id.addTaskButton)
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)

        // 2. Configuración de la Interfaz de Usuario
        setupUserHeader()
        setupLogoutButton()
        setupDatePicker()
        setupRecyclerView()
        setupAddButton()

        // 3. Cargar las tareas iniciales desde la DB (ahora filtradas)
        loadTasks()
    }

    // =========================================================================
    // CONFIGURACIÓN INICIAL DE LA UI (Igual que antes)
    // =========================================================================

    /**
     * Muestra el identificador del usuario (email).
     */
    private fun setupUserHeader() {
        val userEmail = authManager.getCurrentUserEmail()
        userIdentifierTextView.text = userEmail ?: "Usuario Desconocido"
    }

    /**
     * Configura el botón de Cerrar Sesión y navega de vuelta a MainActivity.
     */
    private fun setupLogoutButton() {
        logoutButton.setOnClickListener {
            authManager.logout()
            Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    /**
     * Muestra un DatePickerDialog al hacer clic en el campo de fecha.
     */
    private fun setupDatePicker() {
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    dateEditText.setText(formattedDate)
                }, year, month, day)

            datePickerDialog.show()
        }
    }

    /**
     * Configura el RecyclerView y su Adaptador con las funciones de callback.
     */
    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(taskList,
            onDeleteClick = { taskId -> deleteTask(taskId) },
            onModifyClick = { task -> setFormForModification(task) }
        )
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.adapter = taskAdapter
    }


    /**
     * Configura el botón de Agregar/Modificar tarea.
     */
    private fun setupAddButton() {
        addTaskButton.setOnClickListener {
            if (taskToModify == null) {
                addTask()
            } else {
                updateTask(taskToModify!!)
            }
        }
    }

    // =========================================================================
    // LÓGICA DE DATOS (CRUD SQLite Multi-Usuario)
    // =========================================================================

    /**
     * Carga todas las tareas del usuario actual desde SQLite y actualiza el RecyclerView.
     */
    private fun loadTasks() {
        // **ACTUALIZACIÓN**: Llama a getAllTasks con el ID del usuario.
        currentUserId?.let { userId ->
            val newTasks = dbHelper.getAllTasks(userId)
            taskAdapter.refreshData(newTasks)
        }
    }

    /**
     * Intenta agregar una nueva tarea a SQLite para el usuario actual.
     */
    private fun addTask() {
        val title = titleEditText.text.toString().trim()
        val date = dateEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        if (title.isEmpty() || date.isEmpty() || description.isEmpty() || currentUserId == null) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // **ACTUALIZACIÓN**: Creamos la Task incluyendo el currentUserId
        val newTask = Task(
            userId = currentUserId!!,
            title = title,
            deliveryDate = date,
            description = description
        )
        dbHelper.insertTask(newTask)
        clearForm()
        loadTasks()
        Toast.makeText(this, "Tarea agregada con éxito.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Prepara el formulario para modificar una tarea existente.
     */
    private fun setFormForModification(task: Task) {
        // La tarea ya incluye el userId, no hace falta cambiar nada aquí.
        taskToModify = task

        titleEditText.setText(task.title)
        dateEditText.setText(task.deliveryDate)
        descriptionEditText.setText(task.description)

        addTaskButton.text = "Guardar Cambios"
        addTaskButton.setBackgroundColor(getColor(R.color.orange_500))

        Toast.makeText(this, "Modificando: ${task.title}", Toast.LENGTH_SHORT).show()
    }

    /**
     * Actualiza la tarea en SQLite y vuelve al modo Agregar.
     */
    private fun updateTask(task: Task) {
        val newTitle = titleEditText.text.toString().trim()
        val newDate = dateEditText.text.toString().trim()
        val newDescription = descriptionEditText.text.toString().trim()

        if (newTitle.isEmpty() || newDate.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos para actualizar.", Toast.LENGTH_SHORT).show()
            return
        }

        // Creamos la Task actualizada, manteniendo el ID y el userId original de la tarea (task).
        val updatedTask = task.copy(
            title = newTitle,
            deliveryDate = newDate,
            description = newDescription
        )

        // **ACTUALIZACIÓN**: updateTask ahora usa el userId interno de 'updatedTask' para verificar.
        dbHelper.updateTask(updatedTask)
        clearForm()
        loadTasks()
        Toast.makeText(this, "Tarea actualizada con éxito.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Elimina una tarea después de una confirmación del usuario.
     */
    private fun deleteTask(taskId: Int?) {
        if (taskId == null || currentUserId == null) return

        AlertDialog.Builder(this)
            .setTitle("Confirmar Borrado")
            .setMessage("¿Estás seguro de que quieres eliminar esta tarea? Esta acción es permanente.")
            .setPositiveButton("Sí, Eliminar") { dialog, _ ->
                // **ACTUALIZACIÓN**: Pasamos el userId para el borrado seguro.
                dbHelper.deleteTask(taskId, currentUserId!!)
                loadTasks()
                Toast.makeText(this, "Tarea eliminada.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Limpia el formulario, restablece los campos y lo devuelve al modo "Agregar".
     */
    private fun clearForm() {
        titleEditText.setText("")
        dateEditText.setText("")
        descriptionEditText.setText("")

        taskToModify = null
        addTaskButton.text = "Agregar Tarea"
        addTaskButton.setBackgroundColor(getColor(R.color.green_500))
    }
}