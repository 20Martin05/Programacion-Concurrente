package controlador;

import modelo.Persona;
import modelo.PersonaDAO;
import vista.ventana;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExportacionControlador extends SwingWorker<Void, Void> {

    private final ventana delegado;
    private final List<Persona> contactos;
    private final File archivo;

    public ExportacionControlador(ventana delegado, List<Persona> contactos, File archivo) {
        this.delegado = delegado;
        this.contactos = new ArrayList<>(contactos);
        this.archivo = archivo;
    }

    @Override
    protected Void doInBackground() throws Exception {
        PersonaDAO persona = new PersonaDAO(new Persona());
        persona.exportarCSV(archivo, contactos);
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
            new NotificacionControlador(this.delegado, "msg.notificacion.exportado", "Exportación completada con éxito").start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            new NotificacionControlador(this.delegado, "msg.notificacion.exportado.interrumpido", "Exportación interrumpida", true).start();
        } catch (ExecutionException e) {
            new NotificacionControlador(this.delegado, "msg.notificacion.exportado.error", "Error al exportar archivo", true).start();
        }
    }
}