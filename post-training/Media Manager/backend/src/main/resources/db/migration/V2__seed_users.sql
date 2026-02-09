-- V2: Seed sample users (development only)
-- Plaintext passwords will be converted to bcrypt on application startup by PasswordInitializer.
-- customer1 -> password: customer1pass
-- employee1 -> password: employee1pass
INSERT INTO app_user (username, password, role, customer_id) VALUES
('customer1', 'customer1pass', 'CUSTOMER', 1),
('employee1', 'employee1pass', 'EMPLOYEE', NULL);
