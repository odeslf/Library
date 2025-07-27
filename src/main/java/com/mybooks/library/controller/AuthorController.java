package com.mybooks.library.controller;

import com.mybooks.library.data.AuthorDTO;
import com.mybooks.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService service;

    @GetMapping("/{id}")
    public AuthorDTO findById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @GetMapping
    public List<AuthorDTO> findAll(){
        return service.findAll();
    }

    @PostMapping
    public AuthorDTO create(@RequestBody AuthorDTO author){
        return service.create(author);
    }

    @PutMapping
    public AuthorDTO updateAuthor(@RequestBody AuthorDTO author) {
        return service.update(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
