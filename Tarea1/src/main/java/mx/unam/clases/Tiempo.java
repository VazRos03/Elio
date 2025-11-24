package mx.unam.clases;

public class Tiempo {
    private int hora;   // 0 - 23
    private int minuto; // 0 - 59
    private int segundo; // 0 - 59

    public void establecerTiempo(int h, int m, int s) {
        if ((h >= 0 && h < 24) && (m >= 0 && m < 60) && (s >= 0 && s < 60)) {
            hora = h;
            minuto = m;
            segundo = s;
        } else {
            throw new IllegalArgumentException("hora, minuto y/o segundo estaban fuera de rango");
        }
    }

    public String aStringUniversal() {
        return String.format("%02d:%02d:%02d", hora, minuto, segundo);
    }

    public String toString() {
        return String.format("%d:%02d:%02d %s",
                ((hora == 0 || hora == 12) ? 12 : hora % 12),
                minuto, segundo, (hora < 12 ? "AM" : "PM"));
    }
}
