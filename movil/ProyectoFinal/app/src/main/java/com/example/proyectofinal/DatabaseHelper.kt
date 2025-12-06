package com.example.proyectofinal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Gestor de la Base de Datos SQLite.
 * Hereda de SQLiteOpenHelper para manejar la creación y la versión de la base de datos.
 * Versión 2: Implementa seguridad de datos por usuario (user_id).
 */
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Constantes de la Base de Datos
        private const val DATABASE_NAME = "TasksDB"
        // Aumentamos la versión para que se ejecute onUpgrade y se cree la nueva columna.
        private const val DATABASE_VERSION = 2
        private const val TABLE_TASKS = "tasks"

        // Nombres de las columnas
        private const val COLUMN_ID = "id"
        private const val COLUMN_USER_ID = "user_id" // ¡NUEVO! Columna para filtrar
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DATE = "delivery_date"
        private const val COLUMN_DESCRIPTION = "description"
    }

    // ----------------------------------------------------
    // CREACIÓN Y ACTUALIZACIÓN
    // ----------------------------------------------------

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_TASKS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USER_ID TEXT NOT NULL," + // Aseguramos que el ID del usuario se almacene
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_DESCRIPTION TEXT)"
        db.execSQL(createTableQuery)
        Log.d("DatabaseHelper", "Tabla 'tasks' creada con columna user_id.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si hay un cambio de versión, eliminamos y recreamos la tabla
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
            onCreate(db)
            Log.d("DatabaseHelper", "Base de datos actualizada a la versión 2. Tabla recreada con user_id.")
        }
    }

    // ----------------------------------------------------
    // OPERACIONES CRUD (Filtradas por userId)
    // ----------------------------------------------------

    /**
     * Agrega una nueva tarea a la base de datos, incluyendo el ID del usuario propietario.
     */
    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, task.userId) // GUARDAMOS el ID del usuario
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DATE, task.deliveryDate)
            put(COLUMN_DESCRIPTION, task.description)
        }
        db.insert(TABLE_TASKS, null, values)
        db.close()
    }

    /**
     * Recupera TODAS las tareas para un usuario específico.
     * @param userId El ID del usuario logueado.
     * @return Una lista mutable de objetos Task.
     */
    fun getAllTasks(userId: String): MutableList<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase

        // ¡FILTRADO CRUCIAL!: Solo selecciona tareas donde COLUMN_USER_ID coincida.
        val selectQuery = "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_USER_ID = ? ORDER BY $COLUMN_ID DESC"
        val cursor: Cursor = db.rawQuery(selectQuery, arrayOf(userId))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))

                // Aseguramos que el objeto Task se construya con el userId correcto
                val task = Task(id, userId, title, date, description)
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }

    /**
     * Actualiza una tarea existente, verificando que pertenezca al usuario.
     */
    fun updateTask(task: Task) {
        if (task.id == null) return

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DATE, task.deliveryDate)
            put(COLUMN_DESCRIPTION, task.description)
        }
        // Condición doble: Actualizar SOLO si el ID de la tarea y el ID del usuario coinciden
        db.update(
            TABLE_TASKS,
            values,
            "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?",
            arrayOf(task.id.toString(), task.userId)
        )
        db.close()
    }

    /**
     * Elimina una tarea por su ID, verificando que pertenezca al usuario.
     * @param userId El ID del usuario actual.
     */
    fun deleteTask(taskId: Int?, userId: String) {
        if (taskId == null) return
        val db = writableDatabase
        // Condición doble: Borrar SOLO si el ID de la tarea y el ID del usuario coinciden
        db.delete(
            TABLE_TASKS,
            "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?",
            arrayOf(taskId.toString(), userId)
        )
        db.close()
    }
}