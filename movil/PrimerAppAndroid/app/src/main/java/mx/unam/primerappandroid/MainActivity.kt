package mx.unam.primerappandroid


/*Luis Arturo Vazquez Rosales
* Tarea 3
* Cambio de botones */
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    // 1. Definición de constantes públicas para Intent Extras
    // Usamos NAME, CARTOON y FOOD, que son los campos reales capturados.
    companion object {
        const val EXTRA_NAME = "mx.unam.primerappandroid.NAME"
        const val EXTRA_CARTOON = "mx.unam.primerappandroid.CARTOON"
        const val EXTRA_FOOD = "mx.unam.primerappandroid.FOOD"
        // Eliminamos EXTRA_AGE y EXTRA_OCCUPATION ya que no se usan.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencias a los elementos de la vista
        val nameEditText = findViewById<TextInputEditText>(R.id.editTextName)
        val cartoonEditText = findViewById<TextInputEditText>(R.id.editTextCartoon)
        val foodEditText = findViewById<TextInputEditText>(R.id.editTextFood)
        val greetButton = findViewById<MaterialButton>(R.id.buttonGreet)

        // 2. Definir la acción al hacer clic en el botón
        greetButton.setOnClickListener {

            // Obtener el texto de los campos de entrada
            val name = nameEditText.text.toString().trim()
            val cartoon = cartoonEditText.text.toString().trim()
            val food = foodEditText.text.toString().trim()

            // Comprobar si los campos están vacíos
            if (name.isEmpty() || cartoon.isEmpty() || food.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 3. Crear el Intent para iniciar SaludoActivity y pasar los datos
            val intent = Intent(this, SaludoActivity::class.java).apply {
                // Pasamos los datos que sí existen en la UI
                putExtra(EXTRA_NAME, name)
                putExtra(EXTRA_CARTOON, cartoon)
                putExtra(EXTRA_FOOD, food)
            }
            startActivity(intent)
        }
    }
}