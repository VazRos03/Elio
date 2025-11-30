package com.example.apparchivo


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.*

// Constante para el Logcat
private const val TAG = "AppArchivoLog"
private const val FILENAME_INTERNAL = "data_internal.txt"
private const val FILENAME_EXTERNAL = "data_external.txt"

class MainActivity : AppCompatActivity() {

    // Vistas de la UI
    private lateinit var editTextData: EditText
    private lateinit var textViewContent: TextView
    private lateinit var btnWriteInternal: Button
    private lateinit var btnReadInternal: Button
    private lateinit var btnWriteExternal: Button
    private lateinit var btnReadExternal: Button
    private lateinit var btnReadRaw: Button
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso otorgado. Informar al usuario.
                Toast.makeText(this, "Permiso de Almacenamiento OTORGADO.", Toast.LENGTH_SHORT).show()
            } else {
                // Permiso denegado. Informar al usuario.
                Toast.makeText(this, "Permiso de Almacenamiento DENEGADO. El almacenamiento externo no funcionará.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asegúrate de que 'activity_main' exista en 'res/layout/'
        setContentView(R.layout.activity_main)

        editTextData = findViewById(R.id.editTextData)
        textViewContent = findViewById(R.id.textViewContent)
        btnWriteInternal = findViewById(R.id.btnWriteInternal)
        btnReadInternal = findViewById(R.id.btnReadInternal)
        btnWriteExternal = findViewById(R.id.btnWriteExternal)
        btnReadExternal = findViewById(R.id.btnReadExternal)
        btnReadRaw = findViewById(R.id.btnReadRaw)

        // Almacenamiento Interno
        btnWriteInternal.setOnClickListener {
            writeInternalFile(editTextData.text.toString())
        }
        btnReadInternal.setOnClickListener {
            readInternalFile()
        }

        // Almacenamiento Externo (Requiere Permisos)
        btnWriteExternal.setOnClickListener {
            if (checkExternalStoragePermission()) {
                writeExternalFile(editTextData.text.toString())
            } else {
                requestStoragePermission()
            }
        }
        btnReadExternal.setOnClickListener {
            if (checkExternalStoragePermission()) {
                readExternalFile()
            } else {
                requestStoragePermission()
            }
        }

        // Recurso Raw
        btnReadRaw.setOnClickListener {
            readRawFile(R.raw.data)
        }
    }

    private fun checkExternalStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Solicita el permiso de almacenamiento al usuario.
     */
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Toast.makeText(this, "Acceso a almacenamiento implícito para directorios de la aplicación.", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Interno ---

    private fun writeInternalFile(data: String) {
        try {
            // MODE_PRIVATE reemplaza el contenido si el archivo existe
            val fileOutputStream: FileOutputStream = openFileOutput(FILENAME_INTERNAL, MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
            Toast.makeText(this, "Escritura Interna Exitosa.", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Escritura Interna Exitosa en: ${filesDir}/$FILENAME_INTERNAL")
        } catch (e: Exception) {
            Log.e(TAG, "Error al escribir en almacenamiento interno: ${e.message}")
            Toast.makeText(this, "Error al escribir en interno.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readInternalFile() {
        try {
            val fileInputStream: FileInputStream = openFileInput(FILENAME_INTERNAL)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var text: String?
            while (bufferedReader.readLine().also { text = it } != null) {
                stringBuilder.append(text)
            }
            fileInputStream.close()

            val content = stringBuilder.toString()
            textViewContent.text = "--- Lectura Interna ($FILENAME_INTERNAL) ---\n$content"
            Log.d(TAG, "Lectura Interna Exitosa: $content")

        } catch (e: FileNotFoundException) {
            textViewContent.text = "--- Lectura Interna ---\nArchivo no encontrado."
            Log.e(TAG, "Archivo Interno no encontrado: $FILENAME_INTERNAL")
        } catch (e: Exception) {
            Log.e(TAG, "Error al leer en almacenamiento interno: ${e.message}")
            textViewContent.text = "--- Lectura Interna ---\nError de lectura: ${e.message}"
        }
    }

    // --- Externo (Resuelve el error de "Denegado" al solicitar permiso) ---

    private fun isExternalStorageWritable(): Boolean {
        // Verifica si el almacenamiento externo está disponible para escribir
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun writeExternalFile(data: String) {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Almacenamiento Externo NO montado.", Toast.LENGTH_LONG).show()
            return
        }
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), FILENAME_EXTERNAL)

        try {
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
            Toast.makeText(this, "Escritura Externa Exitosa en ${file.absolutePath}.", Toast.LENGTH_LONG).show()
            Log.d(TAG, "Escritura Externa Exitosa: ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Error al escribir en almacenamiento externo: ${e.message}", e)
            Toast.makeText(this, "Error al escribir en externo: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun readExternalFile() {
        // No es necesario verificar si está montado para leer archivos de la propia app

        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), FILENAME_EXTERNAL)

        try {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var text: String?
            while (bufferedReader.readLine().also { text = it } != null) {
                stringBuilder.append(text).append("\n") // Añadir salto de línea
            }
            fileInputStream.close()

            val content = stringBuilder.toString()
            textViewContent.text = "--- Lectura Externa ($FILENAME_EXTERNAL) ---\n$content"
            Log.d(TAG, "Lectura Externa Exitosa: $content")

        } catch (e: FileNotFoundException) {
            textViewContent.text = "--- Lectura Externa ---\nArchivo no encontrado: ${file.absolutePath}"
            Log.e(TAG, "Archivo Externo no encontrado: ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Error al leer en almacenamiento externo: ${e.message}")
            textViewContent.text = "--- Lectura Externa ---\nError de lectura: ${e.message}"
        }
    }

    private fun readRawFile(resourceId: Int) {
        try {
            val inputStream = resources.openRawResource(resourceId)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            inputStream.close()

            val content = stringBuilder.toString()
            textViewContent.text = "--- Lectura Raw (R.raw/data.txt) ---\n$content"
            Toast.makeText(this, "Lectura Raw Exitosa.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e(TAG, "Error al leer recurso raw: ${e.message}")
            textViewContent.text = "--- Lectura Raw ---\nError de lectura o archivo no encontrado."
            // Sugiere al usuario que cree el archivo R.raw.data
            Toast.makeText(this, "Error. Asegúrate de crear res/raw/data.txt", Toast.LENGTH_LONG).show()
        }
    }
}