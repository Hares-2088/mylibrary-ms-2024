USE `books-db`;

DROP TABLE IF EXISTS books;
create table if not exists books (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book_id VARCHAR(36),
    author_id VARCHAR(100),
    title VARCHAR(100),
    publication_year DATE,
    genre VARCHAR(50),
    description VARCHAR(100),
    available_copies VARCHAR(17)
    );

DROP TABLE IF EXISTS authors;
create table if not exists authors (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    author_id VARCHAR(36),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    biography VARCHAR(200),
    city VARCHAR (50),
    province VARCHAR (50),
    country VARCHAR (50)
    );
