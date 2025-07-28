package com.mybooks.library.repository;

import com.mybooks.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByBooksId(Long bookId);
}
