-- Run once as the MySQL admin user:
--   Linux:   sudo mysql < schema.sql
--   Windows: mysql -u root -p < schema.sql
-- Change the password below before running, and use the same value in DB_PASSWORD.

CREATE DATABASE IF NOT EXISTS gita CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- App user: local connections only, rights on this database only (not root)
CREATE USER IF NOT EXISTS 'gita_app'@'localhost' IDENTIFIED BY 'CHANGE_ME_strong_password';
CREATE USER IF NOT EXISTS 'gita_app'@'127.0.0.1' IDENTIFIED BY 'CHANGE_ME_strong_password';
GRANT SELECT, INSERT ON gita.* TO 'gita_app'@'localhost';
GRANT SELECT, INSERT ON gita.* TO 'gita_app'@'127.0.0.1';
FLUSH PRIVILEGES;

USE gita;

CREATE TABLE IF NOT EXISTS quotes (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    verse      VARCHAR(20)   NOT NULL,
    text       VARCHAR(1000) NOT NULL,
    meaning    VARCHAR(1000) NOT NULL DEFAULT '',
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed rows, so the page shows something on the first load
INSERT INTO quotes (verse, text, meaning) VALUES
('2.47', 'karmany evadhikaras te ma phalesu kadacana',
 'You have a right to your work, never to its results.'),
('6.5', 'uddhared atmanatmanam natmanam avasadayet',
 'Lift yourself up by your own mind; do not drag yourself down.');
