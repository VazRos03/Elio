package mx.unam.primerappandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity
/*Luis Arturo Vazquez Rosales
* Tarea 2 IA
* Agregar dos Campos mas en el saludo
* En nuestro caso agregamos para preguntar la edad y la ocupacion del usuario en
* la aplicacion
* En esta parte cumplimos la replica del ejercicio solicitado por el 
* Profesor Elio Vega*/
class MainActivity : AppCompatActivity() {

    // Constantes para las claves de los datos (EXTRAS) que pasaremos
    companion object {
        const val EXTRA_NAME = "mx.unam.primerappandroid.NAME"
        // Nuevas constantes para Caricatura y Comida
        const val EXTRA_CARTOON = "mx.unam.primerappandroid.CARTOON"
        const val EXTRA_FOOD = "mx.unam.primerappandroid.FOOD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Obtener referencias de los 3 campos de texto (EditText)
        // Aunque el XML usa TextInputEditText, Kotlin los referencia como EditText
        val nameInput = findViewById<EditText>(R.id.editTextName)
        val cartoonInput = findViewById<EditText>(R.id.editTextCartoon)
        val foodInput = findViewById<EditText>(R.id.editTextFood)

        // 2. Obtener la referencia del botón
        val greetButton = findViewById<MaterialButton>(R.id.buttonGreet)

        // 3. Configurar el listener para el clic del botón
        greetButton.setOnClickListener {
            // A. Obtener los 3 valores ingresados por el usuario, eliminando espacios
            val name = nameInput.text.toString().trim()
            val cartoon = cartoonInput.text.toString().trim()
            val food = foodInput.text.toString().trim()

            // B. Crear el Intent para navegar a SaludoActivity
            val intent = Intent(this, SaludoActivity::class.java).apply {
                // C. Agregar los 3 datos al Intent usando las claves (Extras)
                putExtra(EXTRA_NAME, name)
                putExtra(EXTRA_CARTOON, cartoon)
                putExtra(EXTRA_FOOD, food)
            }

            // D. Iniciar la nueva Activity
            startActivity(intent)
        }
    }
}