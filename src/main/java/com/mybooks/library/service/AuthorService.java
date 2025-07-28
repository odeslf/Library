package com.mybooks.library.service;

import com.github.dozermapper.core.Mapper;
import com.mybooks.library.controller.AuthorController;
import com.mybooks.library.data.AuthorDTO;
import com.mybooks.library.exception.RequiredObjectIsNullException;
import com.mybooks.library.exception.ResourceNotFoundException;
import com.mybooks.library.model.Author;
import com.mybooks.library.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@Service
public class AuthorService {


    private AuthorRepository repository;
    private final Mapper mapper;

    private Logger log = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    public AuthorService(Mapper mapper, AuthorRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<AuthorDTO> findAll(){

        log.info("Finding all authors");
        List<AuthorDTO> authors = repository.findAll().stream()
                .map(author -> mapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());

        for (AuthorDTO author : authors) {
            AddHateoasLinks(author);
        }
        return authors;
    }

    public AuthorDTO findById(Long id){

        log.info("Finding author by id {}", id);
        var entity = repository.findById(id).orElseThrow(() -> {
                    log.warn("Author with id {} not found", id);
                    return new ResourceNotFoundException("Author with id " + id + " not found");
                });
        AuthorDTO dto = mapper.map(entity, AuthorDTO.class);
        AddHateoasLinks(dto);
        return dto;
    }


    public AuthorDTO create(AuthorDTO authorDTO){
        if(authorDTO == null) {
            log.error("Attemped to create an author with null object");
            throw new RequiredObjectIsNullException();
        }
        log.info("Creating author {}", authorDTO);
        var entity = mapper.map(authorDTO, Author.class);
        var persisted = repository.save(entity);
        AuthorDTO createdDTO = mapper.map(persisted, AuthorDTO.class);
        AddHateoasLinks(createdDTO);
        log.info("Successfully created author {}", createdDTO);
        return createdDTO;
    }


    public AuthorDTO update(AuthorDTO authorDTO){

        if(authorDTO == null) {
            log.error("Attemped to update an author with null object");
            throw new RequiredObjectIsNullException();
        }
        if (authorDTO.getId() == null) {
            log.error("Attemped to update an author without providing an ID");
            throw new RequiredObjectIsNullException();
        }
        log.info("Updating author {}", authorDTO);
        var entity = repository.findById(authorDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Author with id {} not found", authorDTO.getId());
                    return new ResourceNotFoundException("Author with id " + authorDTO.getId() + " not found");
                });

       mapper.map(authorDTO, entity);

        var persisted = repository.save(entity);
        AuthorDTO updatedDTO = mapper.map(persisted, AuthorDTO.class);

        AddHateoasLinks(updatedDTO);
        log.info("Successfully updated author {}", updatedDTO);

        return updatedDTO;
    }

    public void delete(Long id){
        log.info("Deleting author with id {}", id);
        Author entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        repository.delete(entity);
    }

    private void AddHateoasLinks(AuthorDTO dto) {
        Long id = dto.getId();
        dto.add(linkTo(methodOn(AuthorController.class).findById(id)).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(AuthorController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(AuthorController.class).create(null)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(AuthorController.class).updateAuthor(null)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(AuthorController.class).delete(id)).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(AuthorController.class).findBooksByAuthor(id)).withRel("books").withType("GET"));

    }
}
