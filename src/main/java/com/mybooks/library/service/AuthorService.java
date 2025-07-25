package com.mybooks.library.service;

import com.mybooks.library.data.AuthorDTO;
import com.mybooks.library.exception.RequiredObjectIsNullException;
import com.mybooks.library.exception.ResourceNotFoundException;
import com.mybooks.library.mapper.DozerMapper;
import com.mybooks.library.model.Author;
import com.mybooks.library.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mybooks.library.mapper.DozerMapper.*;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repository;

    private Logger log = LoggerFactory.getLogger(AuthorService.class);

    public List<AuthorDTO> findAll(){

        log.info("Finding all authors");
        var authors = parseListObject(repository.findAll(), AuthorDTO.class);
        return authors;
    }

    public AuthorDTO findById(Long id){

        log.info("Finding author by id {}", id);
        var entity = repository.findById(id).orElseThrow(() -> {
                    log.warn("Author with id {} not found", id);
                    return new ResourceNotFoundException("Author with id " + id + " not found");
                });
        AuthorDTO dto = parseObject(entity, AuthorDTO.class);
        return dto;
    }

    public AuthorDTO create(AuthorDTO authorDTO){
        if(authorDTO == null) {
            log.error("Attemped to create an author with null object");
            throw new RequiredObjectIsNullException();
        }
        log.info("Creating author {}", authorDTO);
        var entity = parseObject(authorDTO, Author.class);
        var persisted = repository.save(entity);
        AuthorDTO createdDTO = parseObject(persisted, AuthorDTO.class);
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

        entity.setFirstName(authorDTO.getFirstName());
        entity.setLastName(authorDTO.getLastName());
        entity.setCountry(authorDTO.getCountry());
        entity.setBirthDate(authorDTO.getBirthDate());

        var persisted = repository.save(entity);
        AuthorDTO updatedDTO = parseObject(persisted, AuthorDTO.class);

        log.info("Successfully updated author {}", updatedDTO);
        return updatedDTO;
    }

    public void delete(Long id){
        log.info("Deleting author with id {}", id);
        Author entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        repository.delete(entity);
    }
}
