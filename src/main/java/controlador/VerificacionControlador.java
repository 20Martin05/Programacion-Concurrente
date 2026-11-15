package controlador;

import modelo.Persona;
import vista.ventana;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VerificacionControlador extends Thread {
    private final ventana delegado;
    private final List<Persona> contactos;
    private final ContactosControlador controlador;
    private final Runnable alFinalizarExito;

    public VerificacionControlador(ventana delegado, List<Persona> contactos,
                                   ContactosControlador controlador, Runnable alFinalizarExito) {
        this.delegado = delegado;
        // Snapshot para evitar ConcurrentModificationException mientras se itera en background
        this.contactos = new ArrayList<>(contactos);
        this.controlador = controlador;
        this.alFinalizarExito = alFinalizarExito;
    }

    private String t(String clave, String respaldo) {
        var bundle = delegado.getBundle();
        return bundle != null ? bundle.getString(clave) : respaldo;
    }

    @Override
    public void run() {
        final String[] datos = new String[5];
        try {
            SwingUtilities.invokeAndWait(() -> {
                delegado.btn_add.setEnabled(false);
                datos[0] = delegado.txt_nombres.getText();
                datos[1] = delegado.txt_telefono.getText();
                datos[2] = delegado.txt_email.getText();
                Object sel = delegado.cmb_categoria.getSelectedItem();
                datos[3] = sel != null ? sel.toString() : "";
                datos[4] = String.valueOf(delegado.chb_favorito.isSelected());
            });
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return;
        }

        boolean duplicado = verificarContactoDuplicado(datos[0], datos[1], datos[2], contactos);

        SwingUtilities.invokeLater(() -> {
            try {
                if (duplicado) {
                    JOptionPane.showMessageDialog(delegado,
                            t("msg.contacto.duplicado", "El contacto ya se encuentra registrado."),
                            t("dialog.advertencia.titulo", "Advertencia"),
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    boolean guardado = controlador.agregar(
                            datos[0], datos[1], datos[2], datos[3], Boolean.parseBoolean(datos[4])
                    );
                    if (guardado && alFinalizarExito != null) {
                        alFinalizarExito.run();
                    }
                }
            } finally {
                delegado.btn_add.setEnabled(true);
            }
        });
    }

    public boolean verificarContactoDuplicado(String nombres, String telefono, String email, List<Persona> lista) {
        if (lista == null) return false;
        String nomInput = nombres != null ? nombres.trim().toLowerCase() : "";
        String telInput = telefono != null ? telefono.trim() : "";
        String emailInput = email != null ? email.trim().toLowerCase() : "";

        for (Persona c : lista) {
            if (c == null) continue;
            if (!nomInput.isEmpty() && nomInput.equals(c.getNombre().trim().toLowerCase())) return true;
            if (!telInput.isEmpty() && telInput.equals(c.getTelefono().trim())) return true;
            if (!emailInput.isEmpty() && emailInput.equals(c.getEmail().trim().toLowerCase())) return true;
        }
        return false;
    }
}