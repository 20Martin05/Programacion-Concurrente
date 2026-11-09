package controlador;

import modelo.Persona;
import vista.ventana;

import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class VerificacionControlador extends Thread{
    private static final Logger LOGGER = Logger.getLogger(VerificacionControlador.class.getName());

    private final ventana delegado;
    private final List<Persona> contactos;
    private final ContactosControlador controlador;

    public VerificacionControlador(ventana delegado, List<Persona> contactos, ContactosControlador controlador) {
        this.delegado = delegado;
        this.contactos = contactos;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        boolean verificar = verificarContactoDuplicado(delegado, contactos);
        SwingUtilities.invokeLater(() -> {
            if (verificar) {
                JOptionPane.showMessageDialog(delegado, "Contactos duplicados",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }else {
            String nombres = delegado.txt_nombres.getText();
            String telefono = delegado.txt_telefono.getText();
            String email = delegado.txt_email.getText();

            controlador.agregar(
                    nombres,
                    telefono,
                    email,
                    delegado.cmb_categoria.getSelectedItem().toString(),
                    delegado.chb_favorito.isSelected()
            );
        }
        });
    }

    public boolean verificarContactoDuplicado(ventana delegado, List<Persona> contactos) {
        String nombres = delegado.txt_nombres.getText();
        String telefono =delegado.txt_telefono.getText();
        String email = delegado.txt_email.getText();
        for (Persona contacto : contactos) {
            if (contacto.getNombre().equals(nombres)) {
                return true;
            }
            if (contacto.getTelefono().equals(telefono)) {
                return true;
            }
            if (contacto.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
