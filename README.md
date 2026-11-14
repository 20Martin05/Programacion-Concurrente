# Sistema de Gestión de Contactos — Programación Concurrente

**Proyecto de la Unidad 3 que extiende el sistema de gestión de contactos en Java, Maven y Java Swing incorporando programación concurrente, ejecución asíncrona en segundo plano sin bloqueo de interfaz y sincronización de acceso a archivos.**

El desarrollo mantiene la arquitectura **MVC (Modelo-Vista-Controlador)** e internacionalización (`ResourceBundle` en Español, Inglés y Portugués), desacoplando todas las operaciones costosas de validación, búsqueda, exportación y actualización de reportes fuera del **Event Dispatch Thread (EDT)**.

---

### ⚙️ Características Técnicas

- **Arquitectura MVC Concurrente:** separación entre la interfaz gráfica (`vista`), los controladores asíncronos especializados (`controlador`), y la capa de persistencia sincronizada (`modelo`).
- **Procesamiento en Segundo Plano (`SwingWorker` y `Thread`):**
  - **Búsqueda en vivo (`BusquedaControlador`):** filtrado asíncrono sobre copia defensiva mediante `DocumentListener` sin congelar la escritura en `txt_buscar`.
  - **Validación de duplicados (`VerificacionControlador`):** verificación en hilo dedicado antes de registrar contactos nuevos, con actualización segura en el EDT mediante `SwingUtilities.invokeLater`.
  - **Exportación CSV asíncrona (`ExportacionControlador`):** generación de archivos `.csv` en segundo plano con manejo de `InterruptedException` y `ExecutionException` en `done()`.
  - **Carga visual y reportes (`LogicaVentana`):** barra de progreso (`JProgressBar`) ejecutada en hilo demonio (`barra-progreso-contactos`) y guardado de estadísticas en `SwingWorker`.
- **Sincronización y Seguridad de Hilos (`PersonaDAO`):** exclusión mutua mediante monitores estáticos (`LOCK_EXPORTACION` y `LOCK_ARCHIVO_MAESTRO`) que envuelven de forma atómica los flujos `BufferedWriter` y `BufferedReader`, previniendo condiciones de carrera y corrupción de datos ante lecturas o escrituras simultáneas.
- **Internacionalización (`ResourceBundle`):** soporte completo para Español, Inglés y Portugués, actualizando componentes, diálogos de error/éxito, mensajes de interrupción y reportes estadísticos en tiempo real.
- **Codificación UTF-8:** lectura y escritura consistente con `StandardCharsets.UTF_8` para tildes y caracteres especiales.

---

### 🛠️ Requisitos Previos

- **Java Development Kit (JDK):** versión 21 o superior.
- **Apache Maven:** versión 3.9 o superior.
- **IntelliJ IDEA** (recomendado, por el soporte nativo del diseñador de formularios Swing y `forms_rt`).

---

### 📂 Estructura del Proyecto

```text
src/
├── main/
│   ├── java/
│   │   ├── controlador/
│   │   │   ├── BusquedaControlador.java         # Filtrado asíncrono en tiempo real (SwingWorker)
│   │   │   ├── CategoriasControlador.java       # Gestión y carga de categorías del formulario
│   │   │   ├── ContactosControlador.java        # Validaciones y operaciones CRUD
│   │   │   ├── ExportacionControlador.java      # Exportación CSV en segundo plano (SwingWorker)
│   │   │   ├── Iconos.java                      # Carga de iconos y renderizado de categorías
│   │   │   ├── IdiomaControlador.java           # Gestión del selector de idiomas
│   │   │   ├── LogicaVentana.java               # Coordinador principal, eventos, atajos y progreso
│   │   │   ├── ReporteEstadisticas.java         # Generación y guardado de reportes
│   │   │   ├── TablaContactos.java              # Modelo, ordenamiento y actualización de tabla
│   │   │   └── VerificacionControlador.java     # Validación concurrente de registros duplicados
│   │   ├── modelo/
│   │   │   ├── Persona.java                     # Entidad del contacto
│   │   │   └── PersonaDAO.java                  # Acceso a datos sincronizado con bloqueos estáticos
│   │   └── vista/
│   │       ├── base/
│   │       │   ├── VentanaUI.java               # Componentes generados por IntelliJ
│   │       │   └── VentanaUI.form               # Diseño visual (IntelliJ GUI Designer)
│   │       └── ventana.java                     # Ventana principal y punto de arranque
│   └── resources/
│       ├── files/
│       │   ├── reporte_estadisticas.txt         # Reporte generado (español)
│       │   ├── reporte_estadisticas_en.txt      # Reporte generado (inglés)
│       │   ├── reporte_estadisticas_pt.txt      # Reporte generado (portugués)
│       │   └── tabla_contactos.csv              # Persistencia maestra de contactos
│       ├── images/
│       │   ├── icons/
│       │   │   ├── application/                 # Icono de la aplicación
│       │   │   ├── buttons/                     # Iconos de los botones
│       │   │   └── categories/                  # Iconos de las categorías
│       │   └── screenshots/                     # Capturas de ejecución y esquema del proyecto
│       ├── messages_en.properties               # Textos en Inglés
│       ├── messages_es.properties               # Textos en Español
│       └── messages_pt.properties               # Textos en Portugués
```

---

### 🖥️ Capturas de Ejecución

#### 1. Aplicación en Español
![Aplicación en Español](src/main/resources/images/screenshots/Aplicacion%20en%20el%20Idioma%20Espa%C3%B1ol%20.png)

#### 2. Aplicación en Inglés
![Aplicación en Inglés](src/main/resources/images/screenshots/Aplicacion%20en%20el%20Idioma%20Ingl%C3%A9s%20.png)

#### 3. Aplicación en Portugués
![Aplicación en Portugués](src/main/resources/images/screenshots/Aplicacion%20en%20el%20Idioma%20Portugu%C3%A9s%20.png)

---

### 📥 Obtener el Proyecto

```bash
git clone [https://github.com/20Martin05/Programacion-Concurrente.git](https://github.com/20Martin05/Programacion-Concurrente.git)
cd Programacion-Concurrente
```

También puedes descargar el proyecto en ZIP desde GitHub y descomprimirlo.

---

### 🎨 Diseño y Arquitectura Concurrente

- **Separación de Hilos:** el EDT queda reservado exclusivamente para el renderizado de componentes Swing, delegando operaciones de E/S y búsqueda a hilos trabajadores.
- **Control de Concurrencia en Disco:** `PersonaDAO` serializa las operaciones sobre `tabla_contactos.csv` y archivos exportados evitando corrupción ante accesos concurrentes.
- **Prototipo de Interfaz:** diseño visual con paleta accesible, tipografía `Segoe UI` e iconos por categoría.

![Esquema del Proyecto](src/main/resources/images/screenshots/Esquema%20del%20Proyecto.png)

> 🔗 **Proyecto en Figma:** https://www.figma.com/design/QgQmY2vxKg7GL6av5ryJzb/Dise%C3%B1o-y-Construcci%C3%B3n-de-Interfaces-Gr%C3%A1ficas?node-id=0-1&t=0FKjo7OiWc7pVb6z-1

---

### ▶️ Compilar y Ejecutar

1. Abre el proyecto en IntelliJ IDEA seleccionando el `pom.xml` como proyecto Maven.
2. Ejecuta la clase `vista.ventana` (`src/main/java/vista/ventana.java`).

Desde la terminal (Windows / PowerShell):

```powershell
mvn clean package
java -cp "target/programacion-concurrente-1.1.0.jar;%USERPROFILE%\.m2\repository\com\intellij\forms_rt\7.0.3\forms_rt-7.0.3.jar" vista.ventana
```

Los recursos de la aplicación se encuentran en `src/main/resources` y la dependencia de IntelliJ Forms (`forms_rt`) se descarga automáticamente desde Maven Central.

---

### 🚀 Instrucciones de Uso

- **Agregar contacto:** completa Nombres, Teléfono y Correo, elige la categoría, marca Favorito si aplica y pulsa **Añadir Contacto**. La verificación de duplicados se ejecuta en segundo plano antes de actualizar la tabla.
- **Modificar contacto:** selecciona una fila en la tabla; sus datos se cargan en el formulario. Edita y pulsa **Modificar**.
- **Eliminar contacto:** selecciona una fila y pulsa **Eliminar** o la tecla `Supr`.
- **Buscar en tiempo real:** escribe en el campo de búsqueda; `BusquedaControlador` filtra los resultados en segundo plano sin bloquear la interfaz.
- **Exportar lista a CSV:** pulsa **Exportar Lista** (`Ctrl + E`), selecciona la ruta de destino y la exportación se ejecuta de forma asíncrona notificando al finalizar.
- **Cambiar idioma:** usa el selector de idioma de la interfaz. Todos los textos, diálogos y reportes estadísticos se actualizan al instante.
- **Atajos de teclado:**
  - `Ctrl + G`: agregar contacto.
  - `Ctrl + S`: modificar contacto seleccionado.
  - `Supr / Delete`: eliminar contacto seleccionado.
  - `Ctrl + E`: exportar lista a CSV.
  - `Ctrl + F`: enfocar barra de búsqueda.
  - `Escape`: deseleccionar la fila actual.

---

### 👤 Autor

- **Desarrollado por:** Martin Rody Rosero Játiva
- **Materia:** Programación de Interfaces Gráficas
- **Universidad:** Universidad Politécnica Salesiana
- **Nivel / Bloque:** Tercer Semestre, Bloque I (Unidad 3)