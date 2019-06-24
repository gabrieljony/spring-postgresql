package com.gabriel.springpostgresql.controller;


import com.gabriel.springpostgresql.exception.NotFoundException;
import com.gabriel.springpostgresql.model.Autor;
import com.gabriel.springpostgresql.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    @GetMapping("/autores")
    public List<Autor> getAllAutores() {

        return autorRepository.findAll();
    }

    @GetMapping("/autores/{id}")
    public Autor getAutorByID(@PathVariable Long id) {
        Optional<Autor> optAutor = autorRepository.findById(id);
        if(optAutor.isPresent()) {
            return optAutor.get();
        }else {
            throw new NotFoundException("Autor not found with id " + id);
        }
    }

    @PostMapping("/autores-lista")
    public List<Autor> createAutorLista(@Valid @RequestBody Autor autor) {
        autorRepository.save(autor);
        return autorRepository.findAll();
    }

    @PostMapping("/autores")
    public Autor createAutor(@Valid @RequestBody Autor autor) {
        Autor obj = autorRepository.save(autor);
        return obj;
    }

    @PutMapping("/autores/{id}")
    public Autor updateAutor(@PathVariable Long id,
                                 @Valid @RequestBody Autor autorUpdated) {
        return autorRepository.findById(id)
                .map(autor -> {
                    autor.setNome(autorUpdated.getNome());
                    autor.setEmail(autorUpdated.getEmail());
                    autor.setSenha(autorUpdated.getSenha());
                    return autorRepository.save(autor);
                }).orElseThrow(() -> new NotFoundException("Autor not found with id " + id));
    }

    @DeleteMapping("/autores/{id}")
    public String deleteAutor(@PathVariable Long id) {
        return autorRepository.findById(id)
                .map(autor -> {
                    autorRepository.delete(autor);
                    return "Delete Successfully!";
                }).orElseThrow(() -> new NotFoundException("Autor not found with id " + id));
    }
}
