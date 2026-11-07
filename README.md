# Programación-Concurrente

Aplicación de escritorio Java Swing para la gestión de contactos.

## Requisitos

- JDK 21 o superior
- Maven 3.9 o superior

## Ejecutar

```bash
mvn clean package
java -cp "target/programacion-concurrente-1.1.0.jar;%USERPROFILE%\.m2\repository\com\intellij\forms_rt\7.0.3\forms_rt-7.0.3.jar" vista.ventana
```

Los recursos de la aplicación se encuentran en `src/main/resources` y la
dependencia de IntelliJ Forms se descarga automáticamente desde Maven Central.