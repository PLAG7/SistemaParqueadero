package parqueadero;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vehiculo {
    private String placa;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private String estado;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Vehiculo(String placa, LocalDateTime horaEntrada) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
        this.horaSalida = null;
        this.estado = "DENTRO";
    }

    public Vehiculo(String placa, LocalDateTime horaEntrada, LocalDateTime horaSalida, String estado) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
    }

    public boolean estaDentro() {
        return "DENTRO".equalsIgnoreCase(this.estado);
    }

    public String toLinea() {
        String entradaStr = horaEntrada != null ? horaEntrada.format(FORMATO) : "null";
        String salidaStr = horaSalida != null ? horaSalida.format(FORMATO) : "null";
        return placa + ";" + entradaStr + ";" + salidaStr + ";" + estado;
    }

    public static Vehiculo desdeLinea(String linea) {
        String[] partes = linea.split(";");
        if (partes.length != 4) return null;
        String placa = partes[0];
        LocalDateTime entrada = !"null".equals(partes[1]) ? LocalDateTime.parse(partes[1], FORMATO) : null;
        LocalDateTime salida = !"null".equals(partes[2]) ? LocalDateTime.parse(partes[2], FORMATO) : null;
        String estado = partes[3];
        return new Vehiculo(placa, entrada, salida, estado);
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalDateTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getHoraEntradaFormateada() {
        return horaEntrada != null ? horaEntrada.format(FORMATO) : "";
    }
}