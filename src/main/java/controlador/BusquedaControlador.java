package controlador;

import modelo.Persona;
import vista.ventana;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Logger;

public class BusquedaControlador extends SwingWorker<List<Persona>, Void> {
    private static final Logger LOGGER = Logger.getLogger(BusquedaControlador.class.getName());

    private final ventana delegado;
    private final List<Persona> contactos;
    private final TablaContactos tablaContactos;
    private final String criterio;

    public BusquedaControlador(ventana delegado, List<Persona> contactos,
                               TablaContactos tablaContactos, String textoBusqueda) {
        this.delegado = delegado;
        this.contactos = new ArrayList<>(contactos);
        this.tablaContactos = tablaContactos;
        this.criterio = textoBusqueda != null ? textoBusqueda.toLowerCase().trim() : "";
    }

    private String t(String clave, String respaldo) {
        var bundle = delegado.getBundle();
        return bundle != null ? bundle.getString(clave) : respaldo;
    }

    @Override
    protected List<Persona> doInBackground() {
        List<Persona> filtrar = new ArrayList<>();
        for (Persona contacto : contactos) {
            if (isCancelled()) break;
            if (contacto.getNombre().toLowerCase().contains(criterio)
                    || contacto.getTelefono().toLowerCase().contains(criterio)
                    || contacto.getEmail().toLowerCase().contains(criterio)
                    || contacto.getCategoria().toLowerCase().contains(criterio)) {
                filtrar.add(contacto);
            }
        }
        return filtrar;
    }

    @Override
    protected void done() {
        if (isCancelled()) return;
        try {
            tablaContactos.cargar(get());
        } catch (CancellationException ignored) {
            // Búsqueda reemplazada por una más reciente
        } catch (Exception e) {
            String error = t("err_excepcion", "Error al procesar la búsqueda: " + e.getMessage());
            LOGGER.severe("Error al procesar la búsqueda: " + e.getMessage());
            JOptionPane.showMessageDialog(delegado, error,
                    t("dialog.error.titulo", "Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
