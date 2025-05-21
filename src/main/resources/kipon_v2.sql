-- Conectarse a la base de datos postgres
\c postgres;

-- Verificar si la base de datos 'kipon' ya existe y crearla si no existe
SELECT 'CREATE DATABASE kipon'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'kipon')\gexec

-- Conectarse a la base de datos kipon
\c kipon;

-- Borrar tablas si existen (en orden de dependencias)
DROP TABLE IF EXISTS Transacciones_Ahorro;
DROP TABLE IF EXISTS Usuario_Hucha;
DROP TABLE IF EXISTS Hucha;
DROP TABLE IF EXISTS Perfil;
DROP TABLE IF EXISTS Usuario;

-- Crear la tabla Usuario
CREATE TABLE IF NOT EXISTS Usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    fecha_registro DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Crear la tabla Perfil
CREATE TABLE IF NOT EXISTS Perfil (
    id_perfil SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) UNIQUE,
    direccion TEXT,
    foto_perfil TEXT DEFAULT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);
-- Crear la tabla Hucha
CREATE TABLE IF NOT EXISTS Hucha (
    id_hucha SERIAL PRIMARY KEY,
    nombre_hucha VARCHAR(100) NOT NULL,
    id_administrador INT NOT NULL,
    cantidad_total DECIMAL(10, 2) DEFAULT 0.00 CHECK (cantidad_total >= 0.00),
    objetivo_ahorro DECIMAL(10, 2) DEFAULT 0.00 CHECK (objetivo_ahorro >= 0.00),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_objetivo DATE DEFAULT NULL,
    foto_hucha TEXT DEFAULT NULL,
    FOREIGN KEY (id_administrador) REFERENCES Usuario(id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Crear la tabla Transacciones_ahorro
CREATE TABLE IF NOT EXISTS Transacciones_Ahorro (
    id_ahorro SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_hucha INT NOT NULL,
    cantidad DECIMAL(10, 2) NOT NULL,
    saldo_posterior DECIMAL(10, 2) NOT NULL,
    fecha DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
        ON DELETE CASCADE,
    FOREIGN KEY (id_hucha) REFERENCES Hucha(id_hucha)
        ON DELETE CASCADE
);

-- Crear la tabla Usuario_Hucha
CREATE TABLE IF NOT EXISTS Usuario_Hucha (
    id_usuario INT,
    id_hucha INT,
    fecha_ingreso DATE DEFAULT CURRENT_DATE,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('Administrador', 'Miembro')),
    PRIMARY KEY (id_usuario, id_hucha),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
        ON DELETE CASCADE,
    FOREIGN KEY (id_hucha) REFERENCES Hucha(id_hucha)
        ON DELETE CASCADE
);

-- Insertar datos en la tabla Usuario
INSERT INTO Usuario (nombre_usuario, password, email)
VALUES
  ('juanperez', 'ju4nP3r3z#2025', 'juan.perez@email.com'),
  ('analopez', 'A!n4LoP3z', 'ana.lopez@email.com'),
  ('carlosmartinez', 'C@rlos_Mart1n', 'carlos.martinez@email.com'),
  ('beatrizgarcia', 'B34tr1zG@rc1a', 'beatriz.garcia@email.com'),
  ('davidrodriguez', 'D@v1d_R0dri', 'david.rodriguez@email.com'),
  ('elenasanchez', 'E!n@S@nch3z', 'elena.sanchez@email.com'),
  ('franciscotorres', 'Fr@nc1sc0T0rr3s', 'francisco.torres@email.com'),
  ('ginafernandez', 'G!n@F3rn@nd3z', 'gina.fernandez@email.com'),
  ('hectorgomez', 'H3ct0r#G0m3z', 'hector.gomez@email.com'),
  ('inesruiz', 'In3sRu1z#2025', 'ines.ruiz@email.com'),
  ('josemartinez', 'J0s3M@rt1n3z', 'jose.martinez@email.com'),
  ('mariafernandez', 'M@r14_F3rn@nd3z', 'maria.fernandez@email.com'),
  ('luisgonzalez', 'L#u1s_G0nz@l3z', 'luis.gonzalez@email.com'),
  ('pedroalvarez', 'P3dr0_@lv4r3z', 'pedro.alvarez@email.com'),
  ('luciaperez', 'L!c14P3r3z#2025', 'lucia.perez@email.com'),
  ('migueldiaz', 'M1g@lD!az@25', 'miguel.diaz@email.com'),
  ('raquelsanchez', 'R@qu3lS@nch3z', 'raquel.sanchez@email.com'),
  ('antoniogarcia', '@nt0n10G@rc14', 'antonio.garcia@email.com'),
  ('teresagomez', 'T3r3s@G0m3z', 'teresa.gomez@email.com'),
  ('juancarlosjimenez', 'Ju@nc@rl0sJ!m3n3z', 'juan.carlos.jimenez@email.com');

-- Insertar datos en la tabla Perfil
INSERT INTO Perfil (id_usuario, nombre_completo, telefono, direccion, foto_perfil)
VALUES
  (1, 'Juan Pérez', '612345678', 'Calle Falsa 123, Madrid', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg'),
  (2, 'Ana López', '623456789', 'Avenida de la Paz 234, Barcelona', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747827677631_image.jpg'),
  (3, 'Carlos Martínez', '634567890', 'Calle del Sol 345, Valencia', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-august-de-richelieu-7428874.jpg'),
  (4, 'Beatriz García', '645678901', 'Plaza Mayor 456, Sevilla', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-raphael-bruno-2151354893-32150936.jpg'),
  (5, 'David Rodríguez', '656789012', 'Calle de la Luna 567, Zaragoza', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-beardedbasturds-842980.jpg'),
  (6, 'Elena Sánchez', '667890123', 'Avenida del Río 678, Bilbao', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-oyebade-michael-226041195-32140072.jpg'),
  (7, 'Francisco Torres', '678901234', 'Calle Nueva 789, Málaga', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-conojeghuo-175687.jpg'),
  (8, 'Gina Fernández', '689012345', 'Calle del Mar 890, Alicante', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-olly-3766224.jpg'),
  (9, 'Héctor Gómez', '690123456', 'Carrer de la Rambla 901, Valencia', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-fashion-1466845.jpg'),
  (10, 'Inés Ruiz', '691234567', 'Calle de la Estrella 101, Córdoba', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-nascimento-jr-1451812733-32154720.jpg'),
  (11, 'José Martínez', '692345678', 'Avenida de la Libertad 12, Salamanca', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-kampus-7555845.jpg'),
  (12, 'María Fernández', '693456789', 'Calle del Prado 23, Granada', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-cristian-rojas-10043109.jpg'),
  (13, 'Luis González', '694567890', 'Plaza Mayor 34, Valladolid', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-keynotez-1845923.jpg'),
  (14, 'Pedro Álvarez', '695678901', 'Calle del Sol 45, Toledo', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-ron-lach-10143322.jpg'),
  (15, 'Lucía Pérez', '696789012', 'Carrer de Sant Joan 56, Barcelona', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-cottonbro-8925962.jpg'),
  (16, 'Miguel Díaz', '697890123', 'Avenida Castilla 67, Burgos', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-ron-lach-9893519.jpg'),
  (17, 'Raquel Sánchez', '698901234', 'Calle de la Fuente 78, Segovia', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-anne-cristine-2151523981-32094215.jpg'),
  (18, 'Antonio García', '699012345', 'Paseo del Carmen 89, Valencia', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-shvetsa-6250998.jpg'),
  (19, 'Teresa Gómez', '600123456', 'Calle de la Palmera 90, Sevilla', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-patrick-porto-1570340-4116643.jpg'),
  (20, 'Juan Carlos Jiménez', '601234567', 'Paseo de las Delicias 101, Madrid', 'https://kipon-images.s3.us-east-1.amazonaws.com/profiles/pexels-yaroslav-shuraev-7688490.jpg');

-- Insertar datos en la tabla Hucha
  INSERT INTO Hucha (nombre_hucha, id_administrador, cantidad_total, objetivo_ahorro, fecha_creacion, fecha_objetivo, foto_hucha)
VALUES
  ('Vacaciones en la playa', 1, 750.00, 1500.00, DEFAULT, '2025-08-15', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-fabianwiktor-994605.jpg'),
  ('Proyecto de Emprendimiento', 2, 1200.00, 5000.00, DEFAULT, '2025-12-31', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-fauxels-3184416.jpg'),
  ('Fondo de Emergencia', 3, 800.00, 3000.00, DEFAULT, '2025-06-30', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-clickerhappy-9660.jpg'),
  ('Fiesta de Fin de Año', 4, 1000.00, 2000.00, DEFAULT, '2025-12-01', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-pavel-danilyuk-6405771.jpg'),
  ('Compra de Bicicletas', 5, 1500.00, 12000.00, DEFAULT, '2025-07-10', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-bear-gingerbliss-422527-1102775.jpg'),
  ('Regalo de Boda', 6, 500.00, 2500.00, DEFAULT, '2025-09-25', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-nurseryart-360624.jpg'),
  ('Mantenimiento del Coche', 7, 600.00, 1800.00, DEFAULT, '2025-10-10', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-azli-nawawi-791168-32152042.jpg'),
  ('Estudios de Posgrado', 8, 2000.00, 8000.00, DEFAULT, '2026-01-15', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-yaroslav-shuraev-6279786.jpg'),
  ('Ahorro para Viaje en Familia', 9, 300.00, 900.00, DEFAULT, '2025-05-20', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-josh-willink-11499-1157399.jpg'),
  ('Viaje a Asia', 10, 2500.00, 7000.00, DEFAULT, '2026-03-30', 'https://kipon-images.s3.us-east-1.amazonaws.com/accounts/pexels-tuan-pm-1320993433-32092626.jpg');

-- Insertar las transacciones de ahorro con saldo_posterior
INSERT INTO Transacciones_Ahorro (id_usuario, id_hucha, cantidad, saldo_posterior, fecha)
VALUES
  -- Hucha 1: Vacaciones en la playa (total: 750.00)
  (1, 1, 200.00, 200.00, '2025-01-10'),
  (2, 1, 300.00, 500.00, '2025-02-20'),
  (3, 1, 250.00, 750.00, '2025-03-15'),

  -- Hucha 2: Proyecto de Emprendimiento (total: 1200.00)
  (4, 2, 500.00, 500.00, '2025-02-05'),
  (5, 2, 400.00, 900.00, '2025-04-15'),
  (6, 2, 300.00, 1200.00, '2025-05-10'),

  -- Hucha 3: Fondo de Emergencia (total: 800.00)
  (7, 3, 300.00, 300.00, '2025-03-10'),
  (8, 3, 500.00, 800.00, '2025-04-20'),

  -- Hucha 4: Fiesta de Fin de Año (total: 1000.00)
  (9, 4, 300.00, 300.00, '2025-05-10'),
  (10, 4, 700.00, 1000.00, '2025-06-15'),

  -- Hucha 5: Compra de Bicicletas (total: 1500.00)
  (1, 5, 500.00, 500.00, '2025-02-01'),
  (3, 5, 1000.00, 1500.00, '2025-04-01'),

  -- Hucha 6: Regalo de Boda (total: 500.00)
  (4, 6, 200.00, 200.00, '2025-06-05'),
  (5, 6, 300.00, 500.00, '2025-07-15'),

  -- Hucha 7: Mantenimiento del Coche (total: 600.00)
  (6, 7, 250.00, 250.00, '2025-06-15'),
  (7, 7, 350.00, 600.00, '2025-07-20'),

  -- Hucha 8: Estudios de Posgrado (total: 2000.00)
  (8, 8, 1000.00, 1000.00, '2025-05-05'),
  (9, 8, 1000.00, 2000.00, '2025-07-01'),

  -- Hucha 9: Ahorro para Viaje en Familia (total: 300.00)
  (10, 9, 300.00, 300.00, '2025-04-10'),

  -- Hucha 10: Viaje a Asia (total: 2500.00)
  (1, 10, 1000.00, 1000.00, '2025-03-05'),
  (2, 10, 1500.00, 2500.00, '2025-05-01');

  -- Insertar datos en la tabla Usuario_Hucha
INSERT INTO Usuario_Hucha (id_usuario, id_hucha, fecha_ingreso, rol)
VALUES
  -- Hucha 1: Vacaciones en la playa
  (1, 1, '2025-01-01', 'Administrador'),
  (2, 1, '2025-02-01', 'Miembro'),
  (3, 1, '2025-03-01', 'Miembro'),

  -- Hucha 2: Proyecto de Emprendimiento
  (4, 2, '2025-02-01', 'Administrador'),
  (5, 2, '2025-03-01', 'Miembro'),
  (6, 2, '2025-04-01', 'Miembro'),

  -- Hucha 3: Fondo de Emergencia
  (7, 3, '2025-03-01', 'Administrador'),
  (8, 3, '2025-04-01', 'Miembro'),

  -- Hucha 4: Fiesta de Fin de Año
  (9, 4, '2025-05-01', 'Administrador'),
  (10, 4, '2025-06-01', 'Miembro'),

  -- Hucha 5: Compra de Bicicletas
  (1, 5, '2025-01-15', 'Administrador'),
  (3, 5, '2025-03-10', 'Miembro'),

  -- Hucha 6: Regalo de Boda
  (4, 6, '2025-06-01', 'Administrador'),
  (5, 6, '2025-07-01', 'Miembro'),

  -- Hucha 7: Mantenimiento del Coche
  (6, 7, '2025-06-15', 'Administrador'),
  (7, 7, '2025-07-01', 'Miembro'),

  -- Hucha 8: Estudios de Posgrado
  (8, 8, '2025-05-10', 'Administrador'),
  (9, 8, '2025-06-10', 'Miembro'),

  -- Hucha 9: Ahorro para Viaje en Familia
  (10, 9, '2025-04-01', 'Administrador'),

  -- Hucha 10: Viaje a Asia
  (1, 10, '2025-03-01', 'Administrador'),
  (2, 10, '2025-04-01', 'Miembro');
