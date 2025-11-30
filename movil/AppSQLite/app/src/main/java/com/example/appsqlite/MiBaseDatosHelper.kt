package com.example.appsqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

// 1. Constantes de la Base de Datos
private const val DATABASE_NAME = "MiAgendaDB"
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "contactos"
private const val COLUMN_ID = "id"
private const val COLUMN_NOMBRE = "nombre"
private const val COLUMN_TELEFONO = "telefono"

// 2. Sentencia SQL para la creación de la tabla
private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_NOMBRE TEXT," +
            "$COLUMN_TELEFONO TEXT)"

// 3. Sentencia SQL para eliminar la tabla
private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

class MiBaseDatosHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val applicationContext = context

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        Log.d("SQLite", "Tabla '$TABLE_NAME' creada exitosamente.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Esta política de actualización es simple: descartar los datos y empezar de nuevo
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.d("SQLite", "Base de datos actualizada de V$oldVersion a V$newVersion.")
    }

    // --- MÉTODOS CRUD ---

    fun insertarDato(nombre: String, telefono: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_TELEFONO, telefono)
        }
        // Insertar la nueva fila, devolviendo el ID de la fila
        val newRowId = db.insert(TABLE_NAME, null, values)
        db.close()

        if (newRowId > 0) {
            Toast.makeText(applicationContext, "Contacto $nombre guardado.", Toast.LENGTH_SHORT).show()
            Log.d("SQLite", "INSERT: Nuevo ID $newRowId")
        } else {
            Toast.makeText(applicationContext, "Error al guardar el contacto.", Toast.LENGTH_SHORT).show()
        }
        return newRowId
    }

    fun leerTodosLosDatos(): Cursor {
        val db = this.readableDatabase
        // Consulta para obtener todas las columnas de todas las filas
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun actualizarDato(id: String, nuevoNombre: String, nuevoTelefono: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nuevoNombre)
            put(COLUMN_TELEFONO, nuevoTelefono)
        }

        // Definir 'where' para la actualización
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id)

        val count = db.update(
            TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
        db.close()

        if (count > 0) {
            Toast.makeText(applicationContext, "Contacto ID $id actualizado.", Toast.LENGTH_SHORT).show()
            Log.d("SQLite", "UPDATE: Filas afectadas: $count")
        } else {
            Toast.makeText(applicationContext, "Error al actualizar ID $id.", Toast.LENGTH_SHORT).show()
        }
        return count
    }

    fun eliminarDato(id: String): Int {
        val db = this.writableDatabase
        // Definir 'where' para la eliminación
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id)

        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()

        if (deletedRows > 0) {
            Toast.makeText(applicationContext, "Contacto ID $id eliminado.", Toast.LENGTH_SHORT).show()
            Log.d("SQLite", "DELETE: Filas eliminadas: $deletedRows")
        } else {
            Toast.makeText(applicationContext, "Error: ID $id no encontrado.", Toast.LENGTH_SHORT).show()
        }
        return deletedRows
    }
}