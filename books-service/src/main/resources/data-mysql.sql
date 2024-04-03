-- Authors
INSERT INTO authors (author_id, first_name, last_name, biography, city, province, country)
VALUES
    ('a1', 'John', 'Doe', 'Biography of John Doe', 'New York', 'New York', 'USA'),
    ('a2', 'Jane', 'Smith', 'Biography of Jane Smith', 'London', 'Greater London', 'UK'),
    ('a3', 'Michael', 'Johnson', 'Biography of Michael Johnson', 'Los Angeles', 'California', 'USA'),
    ('a4', 'Emily', 'Brown', 'Biography of Emily Brown', 'Sydney', 'New South Wales', 'Australia'),
    ('a5', 'David', 'Wilson', 'Biography of David Wilson', 'Toronto', 'Ontario', 'Canada'),
    ('a6', 'Emma', 'Jones', 'Biography of Emma Jones', 'Paris', 'Île-de-France', 'France'),
    ('a7', 'William', 'Taylor', 'Biography of William Taylor', 'Berlin', 'Berlin', 'Germany'),
    ('a8', 'Olivia', 'Martinez', 'Biography of Olivia Martinez', 'Madrid', 'Community of Madrid', 'Spain'),
    ('a9', 'Noah', 'Garcia', 'Biography of Noah Garcia', 'Rome', 'Lazio', 'Italy'),
    ('a10', 'Sophia', 'Lee', 'Biography of Sophia Lee', 'Beijing', 'Beijing', 'China');

-- Books
INSERT INTO books (book_id, author_id, title, publication_year, genre, description, available_copies)
VALUES
    ('b1', 'a1', 'Sample Book 1', '2023-01-01', 'Fiction', 'This is a sample description for Sample Book 1.', '5'),
    ('b2', 'a2', 'Sample Book 2', '2020-05-15', 'Non-Fiction', 'This is a sample description for Sample Book 2.', '10'),
    ('b3', 'a3', 'Sample Book 3', '2022-09-30', 'Science Fiction', 'This is a sample description for Sample Book 3.', '8'),
    ('b4', 'a4', 'Sample Book 4', '2021-03-20', 'Fantasy', 'This is a sample description for Sample Book 4.', '3'),
    ('b5', 'a5', 'Sample Book 5', '2019-07-10', 'Mystery', 'This is a sample description for Sample Book 5.', '12'),
    ('b6', 'a6', 'Sample Book 6', '2020-11-05', 'Thriller', 'This is a sample description for Sample Book 6.', '7'),
    ('b7', 'a7', 'Sample Book 7', '2024-02-15', 'Romance', 'This is a sample description for Sample Book 7.', '9'),
    ('b8', 'a8', 'Sample Book 8', '2022-06-25', 'Biography', 'This is a sample description for Sample Book 8.', '6'),
    ('b9', 'a9', 'Sample Book 9', '2023-10-18', 'History', 'This is a sample description for Sample Book 9.', '4'),
    ('b10', 'a10', 'Sample Book 10', '2020-12-30', 'Self-Help', 'This is a sample description for Sample Book 10.', '11');
