//  Tiempo1.java
// La declaración de la clase Tiempo1 mantiene la hora en formato de 24 horas.
package mx.unam;

public class Tiempo1  
{
   private int hora;   // 0 - 23
   private int minuto; // 0 - 59
   private int segundo; // 0 - 59

   // establece un nuevo valor de tiempo, usando la hora universal; lanza una 
   // excepción si la hora, minuto o segundo son inválidos
   public void establecerTiempo( int h, int m, int s )
   {
      // valida la hora, el minuto y el segundo
      if ( ( h >= 0 && h < 24 ) && ( m >= 0 && m < 60 ) &&
         ( s >= 0 && s < 60 ))
         {
            hora = h;
            minuto = m;
            segundo = s;
         } // fin de if
         else
            throw new IllegalArgumentException(
               "hora, minuto y/o segundo estaban fuera de rango");

   } // fin del método establecerTiempo

   // convierte a objeto String en formato de hora universal (HH:MM:SS)
   public String aStringUniversal()
   {
      return String.format( "%02d:%02d:%02d", hora, minuto, segundo );
   } // fin del método aStringUniversal

   // convierte a objeto String en formato de hora estándar (H:MM:SS AM or PM)
   public String toString()
   {
      return String.format( "%d:%02d:%02d %s", 
         ( ( hora == 0 || hora == 12 ) ? 12 : hora % 12 ),
         minuto, segundo, ( hora < 12 ? "AM" : "PM" ) );
   } // fin del método toString
} // fin de la clase Tiempo1

