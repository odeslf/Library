package com.mybooks.library.repository;

import com.mybooks.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorId(Long authorId);
}
