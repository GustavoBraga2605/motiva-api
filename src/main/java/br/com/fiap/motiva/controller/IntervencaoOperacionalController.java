package br.com.fiap.motiva.controller;

import br.com.fiap.motiva.model.IntervencaoOperacional;
import br.com.fiap.motiva.service.IntervencaoOperacionalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// No JSON, "tipoServico" (PULVERIZACAO ou ROCADA_MECANIZADA) escolhe a subclasse: é o polimorfismo da Sprint 2 via HTTP.
@RestController
@RequestMapping("/api/intervencoes")
public class IntervencaoOperacionalController {

    private final IntervencaoOperacionalService service;

    public IntervencaoOperacionalController(IntervencaoOperacionalService service) {
        this.service = service;
    }

    // GET /api/intervencoes             -> todas
    // GET /api/intervencoes?trechoId=3  -> só as do trecho 3
    @GetMapping
    public List<IntervencaoOperacional> listar(@RequestParam(required = false) Integer trechoId) {
        return trechoId == null ? service.listar() : service.listarPorTrecho(trechoId);
    }

    @GetMapping("/{id}")
    public IntervencaoOperacional buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IntervencaoOperacional criar(@RequestBody IntervencaoOperacional intervencao) {
        return service.criar(intervencao);
    }

    @PutMapping("/{id}")
    public IntervencaoOperacional atualizar(@PathVariable Integer id,
                                            @RequestBody IntervencaoOperacional intervencao) {
        return service.atualizar(id, intervencao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
