

INSERT INTO usuarios ( apellido, email, enabled, nombre, password, username) VALUES ( 'Marin','carlos@mail.com', true, 'Carlos', '$2a$10$AB0pXskhG28FaVpnKuS67eInNyzpzqPRHdCxm.kCkLZFDn70MbYLe', 'carmarin');

INSERT INTO usuarios ( apellido, email, enabled, nombre, password, username) VALUES ( 'admin','admin@mail.com', true, 'admin', '$2a$10$1zIveTSjYOzJP/5JJ3Ndu.5ei/35M2nu01jBtFmT9bePZ8M4.75Ya', 'admin');


INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (1,1);

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2,2);

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2,1);