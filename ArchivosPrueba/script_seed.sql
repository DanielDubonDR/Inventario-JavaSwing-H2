-- ============================================================
--  INVENTORY APP - SEED SCRIPT
--  Base de datos H2 en memoria
-- ============================================================

-- Crear tabla de computadoras
CREATE TABLE IF NOT EXISTS computadoras (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    marca       VARCHAR(100) NOT NULL,
    modelo      VARCHAR(100) NOT NULL,
    precio      DECIMAL(10, 2) NOT NULL,
    stock       INT NOT NULL DEFAULT 0,
    imagen_ruta VARCHAR(500)
);

-- Datos de ejemplo
INSERT INTO computadoras (marca, modelo, precio, stock, imagen_ruta) VALUES
    ('Dell',    'Inspiron 15 3000',     4500.00, 12, 'C:\Users\danie\Documents\ArchivosPrueba\Inspiron_15_3000.jpg'),
    ('HP',      'Pavilion 14',          3800.00,  8, 'C:\Users\danie\Documents\ArchivosPrueba\Pavilion_14.jpg'),
    ('Lenovo',  'ThinkPad E14 Gen 5',   6200.00,  5, 'C:\Users\danie\Documents\ArchivosPrueba\ThinkPad_E14_Gen_5.jpg'),
    ('Apple',   'MacBook Air M2',       9999.00,  3, 'C:\Users\danie\Documents\ArchivosPrueba\MacBook_Air_M2.jpg'),
    ('Asus',    'VivoBook 15',          3500.00, 15, 'C:\Users\danie\Documents\ArchivosPrueba\VivoBook_15.jpg');