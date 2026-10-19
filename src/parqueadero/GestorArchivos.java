package parqueadero;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorArchivos {
    private static final String RUTA_VEHICULOS = "vehiculos.txt";
    private static final String RUTA_CAJA = "caja.txt";

    public void guardarVehiculos(List<Vehiculo> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_VEHICULOS))) {
            for (Vehiculo v : lista) {
                pw.println(v.toLinea());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Vehiculo> cargarVehiculos() {
        List<Vehiculo> lista = new ArrayList<>();
        File archivo = new File(RUTA_VEHICULOS);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lista;
        }
        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty()) {
                    Vehiculo v = Vehiculo.desdeLinea(linea);
                    if (v != null) {
                        lista.add(v);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void guardarCaja(Caja caja) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_CAJA))) {
            for (String mov : caja.getMovimientos()) {
                pw.println(mov);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Caja cargarCaja() {
        Caja caja = new Caja();
        File archivo = new File(RUTA_CAJA);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return caja;
        }
        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(";");
                    if (partes.length == 4) {
                        double valor = Double.parseDouble(partes[3].replace(",", "."));
                        caja.agregarMovimientoArchivo(linea, valor);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return caja;
    }
}