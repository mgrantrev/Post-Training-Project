-- Seed test users (plaintext passwords - PasswordInitializer will BCrypt them on startup)
INSERT INTO app_user (username, password, role, customer_id, employee_id, created_at)
VALUES ('customer1', 'password', 'CUSTOMER', 1, NULL, CURRENT_TIMESTAMP);

INSERT INTO app_user (username, password, role, customer_id, employee_id, created_at)
VALUES ('employee1', 'password', 'EMPLOYEE', NULL, 1, CURRENT_TIMESTAMP);

-- Seed artists
INSERT INTO artist (artist_id, name) VALUES (1, 'The Beatles');
INSERT INTO artist (artist_id, name) VALUES (2, 'Pink Floyd');

-- Seed albums
INSERT INTO album (album_id, title, artist_id) VALUES (1, 'Abbey Road', 1);
INSERT INTO album (album_id, title, artist_id) VALUES (2, 'The Wall', 2);

-- Seed tracks
INSERT INTO track (track_id, name, album_id, unit_price) VALUES (1, 'Come Together', 1, 0.99);
INSERT INTO track (track_id, name, album_id, unit_price) VALUES (2, 'Something', 1, 0.99);
INSERT INTO track (track_id, name, album_id, unit_price) VALUES (3, 'Comfortably Numb', 2, 1.29);

-- Seed invoice for customer1
INSERT INTO invoice (customer_id, invoice_date, total) VALUES (1, CURRENT_TIMESTAMP, 3.27);

-- Seed invoice lines linking tracks to customer1
INSERT INTO invoice_line (invoice_id, track_id, unit_price, quantity) VALUES (1, 1, 0.99, 1);
INSERT INTO invoice_line (invoice_id, track_id, unit_price, quantity) VALUES (1, 2, 0.99, 1);
INSERT INTO invoice_line (invoice_id, track_id, unit_price, quantity) VALUES (1, 3, 1.29, 1);

-- Seed support ticket
INSERT INTO support_ticket (customer_id, employee_id, status, subject, body, created_at, updated_at)
VALUES (1, 1, 'OPEN', 'Track not playing', 'I cannot play Come Together', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Seed a response on the ticket
INSERT INTO support_response (ticket_id, employee_id, employee_name, message, created_at)
VALUES (1, 1, 'employee1', 'We are looking into this issue.', CURRENT_TIMESTAMP);
