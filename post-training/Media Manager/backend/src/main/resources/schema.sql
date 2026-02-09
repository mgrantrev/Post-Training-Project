CREATE TABLE IF NOT EXISTS invoice_line (
    invoice_line_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    track_id INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    quantity INT NOT NULL
);
