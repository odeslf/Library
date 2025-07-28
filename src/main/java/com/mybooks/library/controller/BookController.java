package com.mybooks.library.controller;

import com.mybooks.library.data.BookDTO;
import com.mybooks.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping("/{id}")
    public BookDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<BookDTO> findAll(){
        return service.findAllBooks();
    }

    @PostMapping
    public BookDTO create(@RequestBody BookDTO book) {
        return service.create(book);
    }

    @PutMapping
    public BookDTO updateBook(@RequestBody BookDTO book) {
        return service.update(book);
    }

    @DeleteMapping
    public ResponseEntity<?> delete (@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
