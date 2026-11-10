package controlador;

import modelo.Persona;
import vista.ventana;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BusquedaControlador extends Thread{
    private static final Logger LOGGER = Logger.getLogger(BusquedaControlador.class.getName());

    private final ventana delegado;
    private final List<Persona> contactos;
    private final TablaContactos tablaContactos;

    public BusquedaControlador(ventana delegado, List<Persona> contactos, TablaContactos tablaContactos) {
        this.delegado = delegado;
        this.contactos = contactos;
        this.tablaContactos = tablaContactos;
    }

    @Override
    public void run() {
        buscarContactos(delegado, contactos);
    }

    public void buscarContactos(ventana delegado,  List<Persona> contactos) {
        String texto = delegado.txt_buscar.getText();
        List<Persona> buscar;

        buscar = filtrarContactos(texto);
        actualizarTabla(buscar);
    }

    private List<Persona> filtrarContactos(String texto) {
        String criterio = texto.toLowerCase().trim();
        List<Persona> filtrar = new ArrayList<>();
        for (Persona contacto : contactos) {
            if (contacto.getNombre().toLowerCase().contains(criterio)
                    || contacto.getTelefono().toLowerCase().contains(criterio)
                    || contacto.getEmail().toLowerCase().contains(criterio)
                    || contacto.getCategoria().toLowerCase().contains(criterio)) {
                filtrar.add(contacto);
            }
        }
        return filtrar;
    }

    private void actualizarTabla(List<Persona> resultados) {
            SwingUtilities.invokeLater(() -> {
                tablaContactos.cargar(resultados);
            });

    }
}
