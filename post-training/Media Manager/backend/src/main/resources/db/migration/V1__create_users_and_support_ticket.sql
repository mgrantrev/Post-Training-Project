-- V1: Create users and support_ticket tables
CREATE TABLE IF NOT EXISTS app_user (
  id serial PRIMARY KEY,
  username varchar(100) NOT NULL UNIQUE,
  password varchar(200) NOT NULL,
  role varchar(50) NOT NULL,
  customer_id integer,
  employee_id integer,
  created_at timestamp with time zone DEFAULT now()
);

CREATE TABLE IF NOT EXISTS support_ticket (
  id serial PRIMARY KEY,
  customer_id integer NOT NULL,
  employee_id integer,
  status varchar(50) NOT NULL DEFAULT 'OPEN',
  subject varchar(255),
  body text,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_support_ticket_customer ON support_ticket(customer_id);
CREATE INDEX IF NOT EXISTS idx_support_ticket_employee ON support_ticket(employee_id);
