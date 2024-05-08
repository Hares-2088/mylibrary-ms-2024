USE `books-db`;

create table if not exists books (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book_id VARCHAR(36),
    author_name VARCHAR(100),
    title VARCHAR(100),
    publication_year DATE,
    genre VARCHAR(50),
    description VARCHAR(100),
    available_copies VARCHAR(17)
    );

