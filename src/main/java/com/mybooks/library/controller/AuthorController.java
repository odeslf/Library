package com.mybooks.library.controller;

import com.mybooks.library.data.AuthorDTO;
import com.mybooks.library.data.BookDTO;
import com.mybooks.library.service.AuthorService;
import com.mybooks.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService service;

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE,
                        MediaType.APPLICATION_YAML_VALUE})
    public AuthorDTO findById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @GetMapping(value = "/{authorId}/books",
                produces = {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_YAML_VALUE})
    public List<BookDTO> findBooksByAuthor(@PathVariable("authorId") Long authorId) {
        return bookService.findBooksByAuthorId(authorId);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_YAML_VALUE})
    public List<AuthorDTO> findAll(){
        return service.findAll();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
                             MediaType.APPLICATION_XML_VALUE,
                             MediaType.APPLICATION_YAML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE,
                             MediaType.APPLICATION_XML_VALUE,
                             MediaType.APPLICATION_YAML_VALUE})
    public AuthorDTO create(@RequestBody AuthorDTO author){
        return service.create(author);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_YAML_VALUE},
                produces = {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_YAML_VALUE})
    public AuthorDTO updateAuthor(@RequestBody AuthorDTO author) {
        return service.update(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
