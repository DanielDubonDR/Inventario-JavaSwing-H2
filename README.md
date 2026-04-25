# 💻 Inventario de Computadoras
### Ejemplo didáctico — Java 25 · Swing · H2 in-memory · JDBC · MVC

---

## 🏗️ Arquitectura MVC

```
src/main/java/com/dubon/inventoryproject/
│
├── InventoryProject.java             ← Punto de entrada
│
├── models/
│   └── ComputadoraModel.java         ← MODEL: entidad con 4 atributos + imagen
│
├── views/
│   ├── MainView.java                 ← VIEW: ventana principal (JFrame)
│   ├── FormDialog.java               ← VIEW: diálogo de alta/edición (JDialog)
│   ├── SeedChooserDialog.java        ← VIEW: diálogo de selección de archivo semilla (JDialog)
│   └── ComputadoraTableModel.java    ← VIEW: modelo de datos para JTable
│
├── controllers/
│   └── InventarioController.java     ← CONTROLLER: orquesta Model ↔ View
│
├── database/
│   ├── DatabaseManager.java          ← Conexión H2, seed, import, export SQL
│   └── ComputadoraDAO.java           ← JDBC: CREATE / READ / UPDATE / DELETE
│
└── utils/
    └── CsvExporter.java              ← Exportación de reporte a .csv

src/main/resources/
└── seed.sql                          ← Script semilla (tablas + datos de ejemplo)
```

---

## ⚙️ Requisitos

| Herramienta | Versión mínima |
|-------------|---------------|
| JDK         | 21 LTS (o 25) |
| Maven       | 3.9+          |

> Este proyecto utiliza Java 25, pero es compatible con Java 21 LTS. Asegúrate de tener una versión de JDK instalada y configurada en tu sistema.

---


## 🗄️ Base de datos H2 en memoria

### ¿Cómo funciona?

| Momento          | Acción                                                                 |
|------------------|------------------------------------------------------------------------|
| **Al iniciar**   | Permite cargar un archivo de exportación existente                     |
| **Al iniciar**   | Si existe `inventario_export.sql` → lo importa (datos de sesión previa)|
| **Al cerrar**    | Exporta automáticamente toda la BD a `inventario_export.sql`           |

### Archivos generados en el directorio de trabajo

```
inventario_export.sql          ← Persistencia entre sesiones
reporte_inventario_YYYYMMDD_HHmmss.csv  ← Reportes exportados
```

---

## 🖼️ Imágenes

- Las imágenes se cargan desde **rutas absolutas** del sistema de archivos.
- El botón **📂 Buscar** abre un explorador de archivos.
- Formatos soportados: PNG, JPG/JPEG, GIF.
- La ruta se guarda en la BD y persiste entre sesiones.

---

## 📊 Atributos de Computadora

| Campo       | Tipo    | Descripción                        |
|-------------|---------|-------------------------------------|
| `id`        | int     | Clave primaria auto-incremental     |
| `marca`     | String  | Fabricante (Dell, HP, Apple…)       |
| `modelo`    | String  | Nombre/número de modelo             |
| `precio`    | double  | Precio en quetzales                 |
| `stock`     | int     | Unidades disponibles                |
| `imagenRuta`| String  | Ruta absoluta a la imagen (nullable)|

---

## 📁 Script semilla (`seed.sql`)

El archivo `src/main/resources/seed.sql` contiene:
1. `CREATE TABLE IF NOT EXISTS computadoras (...)` — crea la estructura.
2. 6 registros `INSERT INTO` de ejemplo listos para demostrar la aplicación.

Puedes editar este archivo para personalizar los datos iniciales.

---

## 📌 Conceptos que demuestra este ejemplo

| Concepto           | Dónde                                     |
|--------------------|-------------------------------------------|
| **MVC**            | Separación Model / View / Controller       |
| **Java Swing**     | `JFrame`, `JDialog`, `JTable`, `JLabel`   |
| **H2 in-memory**   | `DatabaseManager` — URL `jdbc:h2:mem:`    |
| **JDBC**           | `ComputadoraDAO` — PreparedStatement       |
| **DAO Pattern**    | `ComputadoraDAO` encapsula todo el SQL     |
| **Script SQL**     | `seed.sql` cargado desde resources         |
| **Export SQL**     | `DatabaseManager.exportDatabase()`         |
| **Export CSV**     | `CsvExporter.exportar()`                   |
| **Imagen en UI**   | `ImageIO.read()` → `ImageIcon` en `JLabel`|
| **WindowListener** | Export automático al cerrar la ventana     |
