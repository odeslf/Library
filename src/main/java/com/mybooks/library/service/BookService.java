package com.mybooks.library.service;

import com.mybooks.library.data.BookDTO;
import com.mybooks.library.exception.RequiredObjectIsNullException;
import com.mybooks.library.exception.ResourceNotFoundException;
import com.mybooks.library.mapper.DozerMapper;
import com.mybooks.library.model.Book;
import com.mybooks.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mybooks.library.mapper.DozerMapper.parseListObject;
import static com.mybooks.library.mapper.DozerMapper.parseObject;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private Logger log = LoggerFactory.getLogger(BookService.class);

    public List<BookDTO> findAllBooks() {

        log.info("Finding all books");
        var books = parseListObject(bookRepository.findAll(), BookDTO.class);
        return books;
    }

    public BookDTO findById(Long id) {
        log.info("Finding a book by id {}", id);
        var entity = bookRepository.findById(id).orElseThrow(() ->{
            log.warn("Book with id {} not found", id);
            return new ResourceNotFoundException("Book with id " + id + " not found");
        });
        BookDTO bookDTO = parseObject(entity, BookDTO.class);
        return bookDTO;
    }

    public BookDTO create(BookDTO bookDTO) {
        if(bookDTO == null) {
            log.error("Attempt to create a null BookDTO");
            throw new RequiredObjectIsNullException();
        }
        log.info("Creating a book {}", bookDTO);
        var entity = parseObject(bookDTO, Book.class);
        var persisted = bookRepository.save(entity);
        BookDTO persistedDTO = parseObject(persisted, BookDTO.class);
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

        entity.setTitle(bookDTO.getTitle());
        entity.setGenre(bookDTO.getGenre());
        entity.setPages(bookDTO.getPages());
        entity.setPublicationYear(bookDTO.getPublicationYear());

        var persisted = bookRepository.save(entity);
        BookDTO updatedDTO = parseObject(persisted, BookDTO.class);

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
}
