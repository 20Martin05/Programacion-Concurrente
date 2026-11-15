package controlador;

import vista.ventana;

import javax.swing.*;
import java.awt.Color;

public class NotificacionControlador extends Thread {
    private static final Object LOCK = new Object();
    private static int contadorActivo = 0;

    private final ventana delegado;
    private final String claveOTexto;
    private final String respaldo;
    private final boolean esError;

    public NotificacionControlador(ventana delegado, String claveOTexto, String respaldo) {
        this(delegado, claveOTexto, respaldo, false);
    }

    public NotificacionControlador(ventana delegado, String claveOTexto, String respaldo, boolean esError) {
        this.delegado = delegado;
        this.claveOTexto = claveOTexto;
        this.respaldo = respaldo;
        this.esError = esError;
    }

    public NotificacionControlador(ventana delegado, String mensaje) {
        this(delegado, null, mensaje, false);
    }

    private String obtenerTexto() {
        if (claveOTexto == null || claveOTexto.isBlank()) {
            return respaldo != null ? respaldo : "";
        }
        var bundle = delegado.getBundle();
        if (bundle != null && bundle.containsKey(claveOTexto)) {
            return bundle.getString(claveOTexto);
        }
        return respaldo != null ? respaldo : claveOTexto;
    }

    @Override
    public void run() {
        final int idTurno;
        synchronized (LOCK) {
            idTurno = ++contadorActivo;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                String mensaje = obtenerTexto();
                delegado.lbl_notificacion_real.setForeground(esError ? new Color(180, 40, 40) : new Color(30, 130, 60));
                delegado.lbl_notificacion_real.setText(mensaje);
                delegado.panelContactos.revalidate();
                delegado.panelContactos.repaint();
            } catch (Exception e) {
                // Ignore silent UI error
            }
        });

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                synchronized (LOCK) {
                    if (idTurno == contadorActivo) {
                        delegado.lbl_notificacion_real.setText("");
                    }
                }
            } catch (Exception e) {
                // Ignore silent UI error
            }
        });
    }
}