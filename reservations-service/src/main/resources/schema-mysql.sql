USE `reservations-db`;

DROP TABLE IF EXISTS reservations;
CREATE TABLE reservations (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    reservation_id VARCHAR(36),
    member_id VARCHAR(36),
    book_id VARCHAR(36),
    reservation_date DATE
);
