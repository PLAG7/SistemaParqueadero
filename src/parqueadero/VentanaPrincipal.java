package parqueadero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

public class VentanaPrincipal extends JFrame {
    private JTextField txtPlaca;
    private JButton btnEntrada, btnSalida;
    private JTable tablaVehiculos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalCaja;
    private Parqueadero parqueadero;

    public VentanaPrincipal() {
        parqueadero = new Parqueadero();

        setTitle("Control de Parqueadero");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnSuperior.setBorder(BorderFactory.createTitledBorder("Registro de Vehiculos"));

        pnSuperior.add(new JLabel("Placa:"));
        txtPlaca = new JTextField(12);
        pnSuperior.add(txtPlaca);

        btnEntrada = new JButton("Registrar Entrada");
        pnSuperior.add(btnEntrada);

        btnSalida = new JButton("Registrar Salida");
        pnSuperior.add(btnSalida);

        add(pnSuperior, BorderLayout.NORTH);

        JPanel pnCentral = new JPanel(new BorderLayout());
        pnCentral.setBorder(BorderFactory.createTitledBorder("Vehiculos en el Parqueadero"));

        modeloTabla = new DefaultTableModel(new Object[]{"Placa", "Fecha y Hora de Entrada", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVehiculos = new JTable(modeloTabla);
        pnCentral.add(new JScrollPane(tablaVehiculos), BorderLayout.CENTER);

        add(pnCentral, BorderLayout.CENTER);

        JPanel pnInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnInferior.setBorder(BorderFactory.createEtchedBorder());

        lblTotalCaja = new JLabel("Total en Caja: $0.00");
        lblTotalCaja.setFont(new Font("Arial", Font.BOLD, 14));
        pnInferior.add(lblTotalCaja);

        add(pnInferior, BorderLayout.SOUTH);

        btnEntrada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarEntrada();
            }
        });

        btnSalida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarSalida();
            }
        });

        actualizarTabla();
    }

    public void registrarEntrada() {
        String placa = txtPlaca.getText().trim();
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un numero de placa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean exito = parqueadero.registrarEntrada(placa);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Entrada registrada correctamente para la placa: " + placa.toUpperCase(), "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            txtPlaca.setText("");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "El vehiculo con placa " + placa.toUpperCase() + " ya se encuentra dentro del parqueadero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void registrarSalida() {
        String placa = txtPlaca.getText().trim();
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese la placa del vehiculo que va a salir.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Vehiculo v = parqueadero.registrarSalida(placa);
        if (v != null) {
            long minutos = parqueadero.calcularMinutos(v);
            int horas = parqueadero.calcularHoras(v);
            double valor = parqueadero.calcularValor(v);

            String mensaje = "Salida registrada para: " + v.getPlaca() + "\n"
                    + "Tiempo de permanencia: " + minutos + " minuto(s)\n"
                    + "Horas cobradas: " + horas + "\n"
                    + "Valor a cancelar: $" + String.format(Locale.US, "%.2f", valor);

            JOptionPane.showMessageDialog(this, mensaje, "Cobro de Parqueadero", JOptionPane.INFORMATION_MESSAGE);
            txtPlaca.setText("");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro ningun vehiculo dentro del parqueadero con la placa: " + placa.toUpperCase(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Vehiculo> dentro = parqueadero.listarDentro();
        for (Vehiculo v : dentro) {
            modeloTabla.addRow(new Object[]{v.getPlaca(), v.getHoraEntradaFormateada(), v.getEstado()});
        }
        lblTotalCaja.setText("Total en Caja: $" + String.format(Locale.US, "%.2f", parqueadero.getCaja().getTotal()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
}