package com.mylibrary.books.dataacess.author;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer>{

    Author findByAuthorIdentifier_AuthorId(String authorId);

}
