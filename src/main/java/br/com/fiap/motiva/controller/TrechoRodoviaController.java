package br.com.fiap.motiva.controller;

import br.com.fiap.motiva.model.TrechoRodovia;
import br.com.fiap.motiva.repository.TrechoRodoviaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trechos")
public class TrechoRodoviaController {

    private final TrechoRodoviaRepository repository;

    public TrechoRodoviaController(TrechoRodoviaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TrechoRodovia> listar() {
        return repository.findAll();
    }
}