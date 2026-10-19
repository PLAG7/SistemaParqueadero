package parqueadero;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Parqueadero {
    private static final double TARIFA_HORA = 0.50;
    private List<Vehiculo> vehiculos;
    private Caja caja;
    private GestorArchivos gestor;

    public Parqueadero() {
        this.gestor = new GestorArchivos();
        this.vehiculos = gestor.cargarVehiculos();
        this.caja = gestor.cargarCaja();
    }

    public boolean registrarEntrada(String placa) {
        if (buscarVehiculo(placa) != null) {
            return false;
        }
        Vehiculo nuevo = new Vehiculo(placa.toUpperCase(), LocalDateTime.now());
        vehiculos.add(nuevo);
        gestor.guardarVehiculos(vehiculos);
        return true;
    }

    public Vehiculo registrarSalida(String placa) {
        Vehiculo v = buscarVehiculo(placa);
        if (v == null) {
            return null;
        }
        v.setHoraSalida(LocalDateTime.now());
        v.setEstado("SALIO");
        
        int horas = calcularHoras(v);
        double valor = calcularValor(v);
        
        caja.registrarCobro(v.getPlaca(), horas, valor);
        gestor.guardarVehiculos(vehiculos);
        gestor.guardarCaja(caja);
        
        return v;
    }

    
    //se toman 5 segundos como 1 hora del mundo real
    public int calcularHoras(Vehiculo vehiculo) {
        if (vehiculo.getHoraEntrada() == null || vehiculo.getHoraSalida() == null) {
            return 1;
        }
        long segundos = Duration.between(vehiculo.getHoraEntrada(), vehiculo.getHoraSalida()).toSeconds();
        int horas = (int) Math.ceil(segundos / 5.0);
        if (horas <= 0) {
            horas = 1;
        }
        return horas;
    }

    public long calcularMinutos(Vehiculo vehiculo) {
        if (vehiculo.getHoraEntrada() == null || vehiculo.getHoraSalida() == null) {
            return 0;
        }
        long segundos = Duration.between(vehiculo.getHoraEntrada(), vehiculo.getHoraSalida()).toSeconds();
        return segundos * 12;
    }

    public double calcularValor(Vehiculo vehiculo) {
        return calcularHoras(vehiculo) * TARIFA_HORA;
    }

    public Vehiculo buscarVehiculo(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa) && v.estaDentro()) {
                return v;
            }
        }
        return null;
    }

    public List<Vehiculo> listarDentro() {
        List<Vehiculo> dentro = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.estaDentro()) {
                dentro.add(v);
            }
        }
        return dentro;
    }

    public Caja getCaja() {
        return caja;
    }
}