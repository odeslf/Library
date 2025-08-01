package com.mybooks.library.service;

import com.github.dozermapper.core.Mapper;
import com.mybooks.library.controller.BookController;
import com.mybooks.library.data.BookDTO;
import com.mybooks.library.exception.RequiredObjectIsNullException;
import com.mybooks.library.exception.ResourceNotFoundException;
import com.mybooks.library.model.Author;
import com.mybooks.library.model.Book;
import com.mybooks.library.repository.AuthorRepository;
import com.mybooks.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final Mapper mapper;

    private final Logger log = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(AuthorRepository authorRepository, BookRepository bookRepository, Mapper mapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public List<BookDTO> findAll() {

        log.info("Finding all books");
        List<BookDTO> books =  bookRepository.findAll().stream()
                .map(book -> mapper.map(book, BookDTO.class))
                .collect(Collectors.toList());

        for(BookDTO book : books){
            AddHateoasLinks(book);
        }
        return books;
    }

    public BookDTO findById(Long id) {

        log.info("Finding a book by id {}", id);
        var entity = bookRepository.findById(id).orElseThrow(() ->{
            log.warn("Book with id {} not found", id);
            return new ResourceNotFoundException("Book with id " + id + " not found");
        });
        BookDTO bookDTO = mapper.map(entity, BookDTO.class);
        AddHateoasLinks(bookDTO);
        return bookDTO;
    }

    public List<BookDTO> findBooksByAuthorId(Long authorId) {

        log.info("Finding all books for author with id {}", authorId);
        authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.warn("Author with id {} not found", authorId);
                    return new ResourceNotFoundException("Author with id " + authorId + " not found");
                });
        List<Book> books = bookRepository.findByAuthorId(authorId);

        List<BookDTO> bookDTOS = books.stream()
                .map(book -> mapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        for(BookDTO bookDTO : bookDTOS){
            AddHateoasLinks(bookDTO);
        }
        return bookDTOS;
    }

    public BookDTO create(BookDTO bookDTO) {

        if(bookDTO == null) {
            log.error("Attempt to create a null BookDTO");
            throw new RequiredObjectIsNullException();
        }
        if(bookDTO.getAuthorId() == null) {
            log.error("Author ID is required for book creation");
            throw new RequiredObjectIsNullException();
        }
        log.info("Creating a book {}", bookDTO);

        var entity = mapper.map(bookDTO, Book.class);

        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> {
                    log.warn("Author with id {} not found", bookDTO.getAuthorId());
                    return new ResourceNotFoundException("Author with id " + bookDTO.getAuthorId() + " not found");
                });

        entity.setAuthor(author);

        var persisted = bookRepository.save(entity);
        BookDTO persistedDTO = mapper.map(persisted, BookDTO.class);
        AddHateoasLinks(persistedDTO);

        log.info("Successfully created book {}", persistedDTO);

        return persistedDTO;
    }

    public BookDTO update(BookDTO bookDTO) {

        if(bookDTO == null) {
            log.error("Attempt to update a Book with a null object");
            throw new RequiredObjectIsNullException();
        }
        if(bookDTO.getId() == null) {
            log.error("Attempt to update a Book with a null id");
            throw new RequiredObjectIsNullException();
        }
        log.info("Updating book {}", bookDTO);
        var entity = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Book with id {} not found", bookDTO.getId());
                    return new ResourceNotFoundException("Book with id " + bookDTO.getId() + " not found");
                });

        mapper.map(bookDTO, entity);

        var persisted = bookRepository.save(entity);
        BookDTO updatedDTO = mapper.map(persisted, BookDTO.class);

        AddHateoasLinks(updatedDTO);
        log.info("Successfully updated book {}", updatedDTO);
        return updatedDTO;
    }

    public void delete(Long id){

        log.info("Deleting book witj id {}", id);
        Book entity = bookRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Book with id " + id + "not found"));
        bookRepository.delete(entity);
    }

    private void AddHateoasLinks(BookDTO dto) {
        Long id = dto.getId();
        dto.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(null)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(null)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(id)).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(BookController.class).findAuthorByBookId(id)).withRel("authors").withType("GET"));

    }
}
