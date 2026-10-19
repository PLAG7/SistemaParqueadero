package parqueadero;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Caja {
    private double total;
    private List<String> movimientos;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Caja() {
        this.total = 0.0;
        this.movimientos = new ArrayList<>();
    }

    public void registrarCobro(String placa, int horas, double valor) {
        String fechaSalida = LocalDateTime.now().format(FORMATO);
        String linea = placa + ";" + fechaSalida + ";" + horas + ";" + String.format(Locale.US, "%.2f", valor);
        movimientos.add(linea);
        total += valor;
    }

    public void agregarMovimientoArchivo(String linea, double valor) {
        movimientos.add(linea);
        total += valor;
    }

    public double getTotal() {
        return total;
    }

    public List<String> getMovimientos() {
        return movimientos;
    }
}