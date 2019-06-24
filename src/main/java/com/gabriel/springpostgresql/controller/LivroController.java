package com.gabriel.springpostgresql.controller;

import com.gabriel.springpostgresql.exception.NotFoundException;
import com.gabriel.springpostgresql.model.Livro;
import com.gabriel.springpostgresql.repository.AutorRepository;
import com.gabriel.springpostgresql.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LivroController {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping("/livros")
    public List<Livro> getAllLivro() {

        return livroRepository.findAll();
    }

    @GetMapping("/autores/{autorId}/livros")
    public List<Livro> getContactByAutorId(@PathVariable Long autorId) {

        if(!autorRepository.existsById(autorId)) {
            throw new NotFoundException("autor not found!");
        }

        return livroRepository.findByAutorId(autorId);
    }

//    @PostMapping("/livros")
//    public livro createlivro(@Valid @RequestBody livro livro) {
//        return livroRepository.save(livro);
//    }

    @PostMapping("/autores/{autorId}/livros")
    public Livro addLivro(@PathVariable Long autorId,
                                    @Valid @RequestBody Livro livro) {
        return autorRepository.findById(autorId)
                .map(autor -> {
                    livro.setAutor(autor);
                    return livroRepository.save(livro);
                }).orElseThrow(() -> new NotFoundException("autor not found!"));
    }

    @PutMapping("/autores/{autorId}/livros/{livroId}")
    public Livro updateLivro(@PathVariable Long autorId,
                                       @PathVariable Long livroId,
                                       @Valid @RequestBody Livro livroUpdated) {

        if(!autorRepository.existsById(autorId)) {
            throw new NotFoundException("autor not found!");
        }

        return livroRepository.findById(livroId)
                .map(livro -> {
                    livro.setTitulo(livroUpdated.getTitulo());
                    livro.setPreco(livroUpdated.getPreco());
                    return livroRepository.save(livro);
                }).orElseThrow(() -> new NotFoundException("Livro not found!"));
    }

    @DeleteMapping("/autores/{autorId}/livros/{livroId}")
    public String deleteLivro(@PathVariable Long autorId,
                                   @PathVariable Long livroId) {

        if(!autorRepository.existsById(autorId)) {
            throw new NotFoundException("Autor not found!");
        }

        return livroRepository.findById(livroId)
                .map(livro -> {
                    livroRepository.delete(livro);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("Contact not found!"));
    }
}
