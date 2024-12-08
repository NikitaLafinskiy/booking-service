-- Insert the user
INSERT INTO "user" (email, first_name, last_name, password)
VALUES ('user@gmail.com', 'John', 'Doe', '$2a$10$dsdZZXK.ehErYviA8w0RX.VoW/M2tE7EFNFvjs1OGkD9MJarOMDAG');

-- Create the user-role association
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM "user" u, role r
WHERE u.email = 'user@gmail.com'
AND r.role = 'CUSTOMER';

-- Modify sequences to avoid conflicts
ALTER SEQUENCE user_id_seq RESTART WITH 3;