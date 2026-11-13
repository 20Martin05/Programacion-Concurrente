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

    private String t(String clave, String respaldo) {
        var bundle = delegado.getBundle();
        return bundle != null ? bundle.getString(clave) : respaldo;
    }

    @Override
    protected Void doInBackground() throws Exception {
        PersonaDAO persona = new PersonaDAO(new Persona());
        persona.exportarCSV(archivo, contactos);
        return  null;
    }

    @Override
    protected void done() {
        try {
            get();
            JOptionPane.showMessageDialog(delegado,
                    t("msg.exportado.exito", "Exportado exitosamente en:\n") + archivo.getAbsolutePath());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(delegado,
                    t("msg.exportado.interrumpir", "Exportación interrumpida: ") + archivo.getAbsolutePath(),
                    t("dialog.error.titulo", "Error"), JOptionPane.ERROR_MESSAGE);
        } catch (ExecutionException e) {
            String causa = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            JOptionPane.showMessageDialog(delegado,
                    t("msg.error.exportar", "Error al exportar archivo: ") + causa,
                    t("dialog.error.titulo", "Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
