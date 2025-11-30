package mx.unam.primerappandroid

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SaludoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saludo)

        // 1. Obtener las referencias de las vistas
        val saludoTextView = findViewById<TextView>(R.id.textViewSaludo)
        // Referencias a Caricatura y Comida
        val cartoonTextView = findViewById<TextView>(R.id.textViewCartoon)
        val foodTextView = findViewById<TextView>(R.id.textViewFood)

        // Referencia al botón de regresar
        val finishButton = findViewById<MaterialButton>(R.id.buttonFinish)

        // 2. Obtener los Intent Extras usando las constantes de MainActivity
        val name = intent.getStringExtra(MainActivity.EXTRA_NAME)
        val cartoon = intent.getStringExtra(MainActivity.EXTRA_CARTOON)
        val food = intent.getStringExtra(MainActivity.EXTRA_FOOD)

        // 3. Mostrar el saludo principal (Nombre)
        if (name.isNullOrEmpty()) {
            // CORRECCIÓN: Usamos la cadena de texto directamente
            saludoTextView.text = "¡Hola, usuario anónimo!"
        } else {
            // CORRECCIÓN: Usamos la cadena de texto con interpolación
            saludoTextView.text = "¡Hola, $name!"
        }

        // 4. Mostrar la Caricatura Favorita
        if (cartoon.isNullOrEmpty()) {
            cartoonTextView.text = "Caricatura favorita: No especificada."
        } else {
            cartoonTextView.text = "Tu caricatura favorita es: $cartoon."
        }

        // 5. Mostrar la Comida Favorita
        if (food.isNullOrEmpty()) {
            foodTextView.text = "Comida favorita: No capturada."
        } else {
            foodTextView.text = "Tu comida favorita es: $food."
        }

        // 6. Configurar el botón para cerrar la actividad y regresar
        finishButton.setOnClickListener {
            finish()
        }
    }
}