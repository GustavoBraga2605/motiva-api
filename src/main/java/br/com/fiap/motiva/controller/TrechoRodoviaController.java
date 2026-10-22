package br.com.fiap.motiva.controller;

import br.com.fiap.motiva.model.TrechoRodovia;
import br.com.fiap.motiva.service.TrechoRodoviaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// O Controller só recebe a requisição, chama o Service e devolve a resposta. Regra de negócio fica no Service.
@RestController
@RequestMapping("/api/trechos")
public class TrechoRodoviaController {

    private final TrechoRodoviaService service;

    public TrechoRodoviaController(TrechoRodoviaService service) {
        this.service = service;
    }

    // GET /api/trechos            -> todos
    // GET /api/trechos?vegetacaoMinima=50 -> só os com vegetação >= 50 cm
    @GetMapping
    public List<TrechoRodovia> listar(@RequestParam(required = false) Double vegetacaoMinima) {
        return vegetacaoMinima == null ? service.listar() : service.listarComVegetacaoMinima(vegetacaoMinima);
    }

    @GetMapping("/{id}")
    public TrechoRodovia buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrechoRodovia criar(@RequestBody TrechoRodovia trecho) {
        return service.criar(trecho);
    }

    @PutMapping("/{id}")
    public TrechoRodovia atualizar(@PathVariable Integer id, @RequestBody TrechoRodovia trecho) {
        return service.atualizar(id, trecho);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }

    // PATCH /api/trechos/1/crescimento?taxa=5 -> soma 5 cm ao nível de vegetação
    @PatchMapping("/{id}/crescimento")
    public TrechoRodovia registrarCrescimento(@PathVariable Integer id, @RequestParam Double taxa) {
        return service.registrarCrescimento(id, taxa);
    }
}
